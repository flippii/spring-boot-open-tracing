package com.spring.boot.example.namegeneratorservice.web;

import com.spring.boot.example.namegeneratorservice.service.AnimalNameService;
import com.spring.boot.example.namegeneratorservice.service.ScientistNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static strman.Strman.toKebabCase;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/names")
public class NameResource {

    private final AnimalNameService animalNameService;
    private final ScientistNameService scientistNameService;

    @GetMapping(path = "/random")
    public String name() {
        log.info("Get random name.");

        String scientist = animalNameService.getAnimals();
        String animal = scientistNameService.getScientists();

        return toKebabCase(scientist) + "-" + toKebabCase(animal);
    }

}
