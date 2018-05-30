/**
 * 
 */
package de.haproxyhq.config.amqp;

import java.nio.charset.Charset;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Christian Brinker, evoila.
 *
 */
@Configuration
public class CustomAmqpConfig {

    @Value("${agent.reply}")
    private String replyMessage;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@PostConstruct
	public void configureRabbitTemplate() {
		rabbitTemplate.setReplyTimeout(120000);
		rabbitTemplate.setCorrelationKey(UUID.randomUUID().toString());
	}

	public String getCharset() {
		return Charset.defaultCharset().displayName();
	}

	public String getContentType() {
		return MessageProperties.CONTENT_TYPE_JSON;
	}

	public String getHost() {
		return rabbitTemplate.getConnectionFactory().getHost();
	}

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }
}
