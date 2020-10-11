package com.spring.boot.example.animalnameservice.web;

import com.spring.boot.example.animalnameservice.service.AnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/animals")
public class AnimalNameResource {

    private final AnimalService animalService;

    @GetMapping(path = "/random")
    public String name(@RequestHeader(name = "user_name", required = false) String username) {
        log.info("Get random animal with username: {}.", username);
        return animalService.getRandomAnimal() + " for user with name: " + username;
    }

}
