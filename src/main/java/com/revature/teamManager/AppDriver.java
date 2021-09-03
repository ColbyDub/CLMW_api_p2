package com.revature.teamManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableMongoRepositories(basePackages = "com.revature.teamManager.data.repos")
public class AppDriver {

    public static void main(String[] args) {
        SpringApplication.run(AppDriver.class, args);
    }

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
