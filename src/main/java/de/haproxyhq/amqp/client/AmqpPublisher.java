package de.haproxyhq.amqp.client;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.haproxyhq.config.amqp.CustomAmqpConfig;

/**
 * 
 * @author Christian Brinker.
 *
 */
@Component
public class AmqpPublisher {

    private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private CustomAmqpConfig config;

	public void publishAgentConfig(ObjectId agentId) throws IllegalStateException, TimeoutException {
		final String agentIdAsString = agentId.toString();
		String exchange = agentIdAsString;
		String routingKey = agentIdAsString;
		sendMessage(exchange, routingKey, config.getReplyMessage());
	}

	private void sendMessage(String exchange, String routingKey, String payload)
			throws TimeoutException, IllegalStateException {

		Message message = createMessage(payload);

		Message response = this.rabbitTemplate.sendAndReceive(exchange, routingKey, message);

		log.info("Returning response: " + response);

		if (response == null) {
			throw new TimeoutException("Job is taking too long, probably Agent on HaProxy Service Key Host is!");
		}

		validateResponse(response);
	}

	protected void validateResponse(Message response) throws IllegalStateException {
		String code = new String(response.getBody());
		if (!Objects.equals(code, "OK")) {
			throw new IllegalStateException("An error occured during informing of HaProxyAgent! " +
                    "Return code was '" + code + "' instead of 'OK'.");
		}
	}

	private Message createMessage(String payload) {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentEncoding(config.getCharset());
		messageProperties.setContentType(config.getContentType());
		messageProperties.setCorrelationIdString(newCorrelationId());
		return new Message(payload.getBytes(), messageProperties);
	}

	protected String newCorrelationId() {
		return UUID.randomUUID().toString();
	}
}
