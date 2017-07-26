/**
 * 
 */
package de.haproxyhq.config.nosql;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

import de.haproxyhq.bean.DatabaseBean;
import de.haproxyhq.utils.PackageUtils;

/**
 * 
 * @author Johannes Hiemer, Maximilian Büttner
 *
 */
@Configuration
@EnableMongoRepositories(basePackages = { PackageUtils.NOSQL_REPOSITORIES_PACKAGE })
public class CustomMongoDBRepositoryConfig {
	
	@Configuration
	@Profile("default")
	static class Default extends AbstractMongoConfiguration {

		@Autowired
		private DatabaseBean databaseBean;
		
		private String databaseHost;
		private String databaseUsername;
		private String databasePassword;
		private int databasePort;		
		private String databaseName;
		
		@PostConstruct
		private void initValues() {
			System.out.println(databaseBean.getHost());
			databaseHost = databaseBean.getHost();
			databaseUsername = databaseBean.getUser();
			databasePassword = databaseBean.getPassword();
			databasePort = databaseBean.getPort();
			databaseName = databaseBean.getDatabase();
		}

		@Override
		protected String getDatabaseName() {
			return databaseName;
		}

		@Override
		protected String getMappingBasePackage() {
			return PackageUtils.NOSQL_PACKAGE;
		}

		@Override
		public Mongo mongo() throws Exception {
			MongoClient mongoClient = new MongoClient(new ServerAddress(databaseHost, databasePort),
					getMongoCredentials());
			mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
			return mongoClient;
		}

		protected List<MongoCredential> getMongoCredentials() {
			System.out.println(databaseUsername);
			System.out.println(databaseName);
			System.out.println(databasePassword);
			MongoCredential credential = MongoCredential.createCredential(databaseUsername, databaseName,
					databasePassword.toCharArray());
			List<MongoCredential> credentialList = new ArrayList<>();
			credentialList.add(credential);
			return credentialList;
		}

	}

}