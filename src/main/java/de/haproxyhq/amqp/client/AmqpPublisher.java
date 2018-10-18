package de.haproxyhq.amqp.client;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
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

	@Autowired
	private AsyncRabbitTemplate asyncRabbitTemplate;

	public void publishAgentConfig(String agentId) throws IllegalStateException, TimeoutException {
		String exchange = agentId;
		String routingKey = agentId;
		sendMessage(exchange, routingKey, config.getReplyMessage());
	}

	private void sendMessage(String exchange, String routingKey, String payload)
			throws TimeoutException, IllegalStateException {

		Message message = createMessage(payload);
		AsyncRabbitTemplate.RabbitMessageFuture future=asyncRabbitTemplate.sendAndReceive(exchange,routingKey,message);
		try {
			Message response = future.get();
			if (response == null) {
				throw new TimeoutException("Job is taking too long, probably Agent on HaProxy Service Key Host is!");
			}
			validateResponse(response);
		} catch (InterruptedException e) {
			throw new IllegalStateException("Haproxy Agent Job interrupted");
		} catch (ExecutionException e) {
			throw new IllegalStateException("Haproxy Agent Job Exception Error");
		}

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
		return new Message(payload.getBytes(), messageProperties);
	}
}
