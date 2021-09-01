package com.revature.team_manager.config;

import com.mongodb.client.MongoClient;
import com.revature.team_manager.data.util.MongoClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
