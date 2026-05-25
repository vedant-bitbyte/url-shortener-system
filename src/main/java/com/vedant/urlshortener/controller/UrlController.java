package com.vedant.urlshortener.controller;

import com.vedant.urlshortener.dto.ShortenUrlRequest;
import com.vedant.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/url/shorten")
    public ResponseEntity<String> shorten(@Valid @RequestBody ShortenUrlRequest request) {
        String shortenedUrl = urlService.createShortUrl(request);
        return ResponseEntity.ok(shortenedUrl);
    }

    @GetMapping("/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.redirectToOriginalUrl(shortCode);
        return new RedirectView(originalUrl);
    }
}
