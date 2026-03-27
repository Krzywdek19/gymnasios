package com.krzywdek19.workout_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI workoutServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gymnasios Workout Service API")
                        .version("v1")
                        .description("API for training plans, workout templates, exercise templates, workout sessions, exercise sessions, and set sessions."))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Current environment")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi trainingPlansApi() {
        return GroupedOpenApi.builder()
                .group("training-plans")
                .pathsToMatch("/api/v1/training-plans/**")
                .build();
    }

    @Bean
    public GroupedOpenApi workoutTemplatesApi() {
        return GroupedOpenApi.builder()
                .group("workout-templates")
                .pathsToMatch(
                        "/api/v1/workout-templates/**",
                        "/api/v1/training-plans/*/workout-templates/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi exerciseTemplatesApi() {
        return GroupedOpenApi.builder()
                .group("exercise-templates")
                .pathsToMatch(
                        "/api/v1/exercise-templates/**",
                        "/api/v1/workout-templates/*/exercise-templates/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi workoutSessionsApi() {
        return GroupedOpenApi.builder()
                .group("workout-sessions")
                .pathsToMatch("/api/v1/workout-sessions/**")
                .build();
    }

    @Bean
    public GroupedOpenApi exerciseSessionsApi() {
        return GroupedOpenApi.builder()
                .group("exercise-sessions")
                .pathsToMatch(
                        "/api/v1/exercise-sessions/**",
                        "/api/v1/workout-sessions/*/exercise-sessions/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi setSessionsApi() {
        return GroupedOpenApi.builder()
                .group("set-sessions")
                .pathsToMatch("/api/v1/set-sessions/**")
                .build();
    }
}