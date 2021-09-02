package com.revature.teamManager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.revature.teamManager")
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

//    @Bean
//    public PasswordUtils passwordUtils() {
//        return new PasswordUtils();
//    }

}
