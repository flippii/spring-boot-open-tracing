package com.spring.boot.example.namegeneratorservice.service;

import com.spring.boot.example.namegeneratorservice.configuration.ServiceUrlProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScientistNameService {

    private final RestTemplate restTemplate;

    public ScientistNameService(RestTemplateBuilder restTemplateBuilder, ServiceUrlProperties serviceUrlProperties) {
        this.restTemplate = restTemplateBuilder.rootUri(serviceUrlProperties.getScientistService()).build();
    }

    public String getScientists() {
        return restTemplate.getForObject("/scientists/random", String.class);
    }

}
