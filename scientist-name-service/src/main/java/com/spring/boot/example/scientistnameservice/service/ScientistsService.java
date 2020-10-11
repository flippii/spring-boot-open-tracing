package com.spring.boot.example.scientistnameservice.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ScientistsService {

    private List<String> animalNames;
    private Random random;

    @PostConstruct
    public void init() throws IOException {
        InputStream inputStream = new ClassPathResource("/scientists.txt").getInputStream();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            animalNames = reader.lines().collect(Collectors.toList());
        }

        random = new Random();
    }

    public String getRandomScientist() {
        return animalNames.get(random.nextInt(animalNames.size()));
    }

}
