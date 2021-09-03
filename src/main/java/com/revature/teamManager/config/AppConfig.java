package com.revature.teamManager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("com.revature.teamManager")
@PropertySource("classpath:application.properties")
@Import({AspectConfig.class, WebConfig.class, DataConfig.class})
public class AppConfig {

//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper().findAndRegisterModules();
//    }
//
//    @Bean
//    public PasswordUtils passwordUtils() {
//        return new PasswordUtils();
//    }

}
