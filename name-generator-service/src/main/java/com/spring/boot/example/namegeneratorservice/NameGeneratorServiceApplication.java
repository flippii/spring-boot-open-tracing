package com.spring.boot.example.namegeneratorservice;

import com.spring.boot.example.namegeneratorservice.configuration.ServiceUrlProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ServiceUrlProperties.class})
public class NameGeneratorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NameGeneratorServiceApplication.class, args);
    }

}
