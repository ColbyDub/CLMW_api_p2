package com.revature.teamManager.config;

import com.mongodb.client.MongoClient;
import com.revature.teamManager.data.util.MongoClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class DataConfig {

    @Bean
    public MongoClient mongoClient() {
        return mongoClientFactory().getConnection();
    }

    @Bean
    public MongoClientFactory mongoClientFactory() {
        return new MongoClientFactory();
    }

}
