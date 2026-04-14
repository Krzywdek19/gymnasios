package com.krzywdek19.workout_service.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "secret")
public record JwtProperties(
        String key,
        String issuer
) {
}