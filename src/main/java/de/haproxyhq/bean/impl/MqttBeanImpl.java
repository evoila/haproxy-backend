package de.haproxyhq.bean.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import de.haproxyhq.bean.MqttBean;

@Service
@ConfigurationProperties(prefix="mqtt")
public class MqttBeanImpl implements MqttBean {

	private String host;
	private Client client;
	private Topic topic;
	
	public static class Client {
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}
	
	public static class Topic {
		private String prefix;

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	}

	public String getHost() {
		return host;
	}

	public Client getClient() {
		return client;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	
	public String getClientId() {
		return client.getId();
	}
	
	public String getTopicPrefix() {
		return topic.getPrefix();
	}
}
