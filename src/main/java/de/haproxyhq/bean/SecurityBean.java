package de.haproxyhq.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix="security.token")
public class SecurityBean {

	private String defaultToken;
	private String name;
	
	public String getDefaultToken() {
		return defaultToken;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDefaultToken(String defaultToken) {
		this.defaultToken = defaultToken;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
