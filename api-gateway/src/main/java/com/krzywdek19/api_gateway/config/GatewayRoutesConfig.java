package com.krzywdek19.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    private static final String API_PREFIX = "/api/v1/";

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path(
                                API_PREFIX + "auth/**",
                                API_PREFIX + "users/**"
                        )
                        .uri("lb://user-service")
                )
                .route("workout-service", r -> r
                        .path(
                                API_PREFIX + "training-plans/**",
                                API_PREFIX + "workout-templates/**",
                                API_PREFIX + "exercise-templates/**",
                                API_PREFIX + "workout-sessions/**",
                                API_PREFIX + "exercise-sessions/**",
                                API_PREFIX + "set-sessions/**",
                                API_PREFIX + "workouts/**"
                        )
                        .uri("lb://workout-service")
                )
                .build();
    }
}