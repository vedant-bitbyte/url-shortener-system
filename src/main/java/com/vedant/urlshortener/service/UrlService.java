package com.vedant.urlshortener.service;

import com.vedant.urlshortener.dto.ShortenUrlRequest;
import com.vedant.urlshortener.dto.UrlAnalyticsResponse;
import com.vedant.urlshortener.exception.ResourceNotFoundException;
import com.vedant.urlshortener.model.ShortUrl;
import com.vedant.urlshortener.repository.ShortUrlRepository;
import com.vedant.urlshortener.util.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlService {

    private final ShortUrlRepository shortUrlRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public UrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public String createShortUrl(ShortenUrlRequest request) {
        String shortCode = generateUniqueShortCode();

        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(request.getOriginalUrl())
                .shortCode(shortCode)
                .clickCount(0L)
                .createdAt(LocalDateTime.now())
                .build();

        shortUrlRepository.save(shortUrl);

        return buildShortUrl(shortCode);
    }

    public String redirectToOriginalUrl(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrl.setLastAccessed(LocalDateTime.now());
        shortUrlRepository.save(shortUrl);

        return shortUrl.getOriginalUrl();
    }

    public UrlAnalyticsResponse getUrlAnalytics(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        return new UrlAnalyticsResponse(
                shortUrl.getOriginalUrl(),
                shortUrl.getShortCode(),
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt(),
                shortUrl.getLastAccessed()
        );
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = ShortCodeGenerator.generateShortCode();
        } while (shortUrlRepository.findByShortCode(shortCode).isPresent());
        return shortCode;
    }

    private String buildShortUrl(String shortCode) {
        String base = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return base + "/" + shortCode;
    }
}
