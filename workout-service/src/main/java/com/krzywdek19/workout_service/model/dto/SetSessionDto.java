package com.krzywdek19.workout_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(
        name = "SetSessionDto",
        description = "Set session representation returned by the workout service."
)
public record SetSessionDto(
        @Schema(
                description = "Set session identifier.",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID id,

        @Schema(
                description = "Order of the set inside the exercise session.",
                example = "0"
        )
        Integer orderIndex,

        @Schema(
                description = "Number of repetitions performed.",
                example = "8"
        )
        Integer reps,

        @Schema(
                description = "Weight used in the set.",
                example = "80.0"
        )
        BigDecimal weight,

        @Schema(
                description = "Reps in reserve.",
                example = "2",
                nullable = true
        )
        Integer rir,

        @Schema(
                description = "Whether the set has been completed.",
                example = "true"
        )
        boolean completed
) {
}