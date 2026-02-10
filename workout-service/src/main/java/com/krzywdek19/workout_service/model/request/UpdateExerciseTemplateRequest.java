package com.krzywdek19.workout_service.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateExerciseTemplateRequest(
        @NotBlank String name,
        @NotBlank String notes,
        @NotNull @Min(0) Integer orderIndex,
        @NotNull @Min(1) Integer setsCount
) {}
