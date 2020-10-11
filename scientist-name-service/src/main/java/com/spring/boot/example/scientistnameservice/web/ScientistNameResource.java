package com.spring.boot.example.scientistnameservice.web;

import com.spring.boot.example.scientistnameservice.service.ScientistsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scientists")
public class ScientistNameResource {

    private final ScientistsService scientistsService;

    @GetMapping(path = "/random")
    public String name(@RequestHeader(name = "user_name", required = false) String username) {
        log.info("Get random scientist with username: {}.", username);
        return scientistsService.getRandomScientist() + " for user with name: " + username;
    }

}
