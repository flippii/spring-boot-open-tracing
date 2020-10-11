package com.spring.boot.example.namegeneratorservice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("services.urls")
public class ServiceUrlProperties {

    private String animalService;
    private String scientistService;

}
