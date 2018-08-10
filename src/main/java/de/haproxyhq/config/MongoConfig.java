package de.haproxyhq.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Profile("pcf")
public class MongoConfig extends AbstractMongoConfiguration {



    @Value("${MONGODB_HOST}")
    List<String> mongoHosts;

    @Value("${SPRING_DATA_MONGODB_PORT}")
    int port;

    @Value("${SPRING_DATA_MONGODB_DATABASE}")
    String db;

    @Value("${SPRING_DATA_MONGODB_PASSWORD}")
    String password;

    @Value("${SPRING_DATA_MONGODB_USERNAME}")
    String username;
    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongoClient();
    }

    public MongoClient mongoClient() {
        StringBuilder builder = new StringBuilder(String.format("mongodb://%s:%s@", username, password));
        builder.append(mongoHosts.stream().map(s-> s+":"+port).collect(Collectors.joining(",")));
        builder.append("/");
        builder.append(db);

        return new MongoClient(new MongoClientURI(builder.toString()));
    }
}