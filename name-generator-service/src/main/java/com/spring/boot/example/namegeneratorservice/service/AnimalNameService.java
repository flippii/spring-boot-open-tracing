package com.spring.boot.example.namegeneratorservice.service;

import com.spring.boot.example.namegeneratorservice.configuration.ServiceUrlProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnimalNameService {

    private final RestTemplate restTemplate;

    public AnimalNameService(RestTemplateBuilder restTemplateBuilder, ServiceUrlProperties serviceUrlProperties) {
        this.restTemplate = restTemplateBuilder.rootUri(serviceUrlProperties.getAnimalService()).build();
    }

    public String getAnimals() {
        return restTemplate.getForObject("/animals/random", String.class);
    }

}
