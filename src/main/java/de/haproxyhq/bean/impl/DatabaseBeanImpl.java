package de.haproxyhq.bean.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import de.haproxyhq.bean.DatabaseBean;

@Service
@ConfigurationProperties(prefix="database.nosql")
public class DatabaseBeanImpl implements DatabaseBean {

	private String host;
	private String user;
	private String password;
	private int port;
	private String database;
	
	public String getHost() {
		return host;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setDatabase(String database) {
		this.database = database;
	}
}
