package com.vedant.urlshortener.controller;

import com.vedant.urlshortener.dto.ShortenUrlRequest;
import com.vedant.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@Valid @RequestBody ShortenUrlRequest request) {
        String shortenedUrl = urlService.createShortUrl(request);
        return ResponseEntity.ok(shortenedUrl);
    }
}
