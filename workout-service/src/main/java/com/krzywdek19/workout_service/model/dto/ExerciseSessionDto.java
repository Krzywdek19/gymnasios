package com.krzywdek19.workout_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(
        name = "ExerciseSessionDto",
        description = "Exercise session representation returned by the workout service."
)
public record ExerciseSessionDto(
        @Schema(
                description = "Exercise session identifier.",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID id,

        @Schema(
                description = "Exercise name.",
                example = "Barbell Bench Press"
        )
        String name,

        @Schema(
                description = "Order of the exercise inside the workout session.",
                example = "0"
        )
        Integer orderIndex,

        @Schema(
                description = "Planned number of sets for the exercise.",
                example = "4"
        )
        Integer setsCount,

        @Schema(
                description = "List of performed sets for the exercise session."
        )
        List<SetSessionDto> sets
) {
}