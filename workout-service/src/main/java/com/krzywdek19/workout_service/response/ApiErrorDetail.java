package com.krzywdek19.workout_service.response;

public record ApiErrorDetail(
        String field,
        String message
) {
}
