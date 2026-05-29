package com.vedant.urlshortener.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("project", "Scalable URL Shortener Backend");
        response.put("status", "Running");
        response.put("version", "1.0.0");

        response.put("description",
                "Production-ready URL Shortener built with Spring Boot, JWT Authentication, Redis Caching, MySQL and Docker.");

        response.put("features", new String[]{
                "User Registration & Login",
                "JWT Authentication",
                "URL Shortening",
                "URL Redirection",
                "Analytics Tracking",
                "Redis Caching",
                "Dockerized Deployment"
        });

        response.put("github",
                "https://github.com/YOUR_USERNAME/YOUR_REPOSITORY");

        response.put("postmanCollection",
                "https://documenter.getpostman.com/view/YOUR_COLLECTION_ID");

        response.put("health", "/actuator/health");

        return response;
    }
}