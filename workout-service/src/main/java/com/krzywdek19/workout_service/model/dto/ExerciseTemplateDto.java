package com.krzywdek19.workout_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(
        name = "ExerciseTemplateDto",
        description = "Exercise template representation returned by the workout service."
)
public record ExerciseTemplateDto(
        @Schema(
                description = "Exercise template identifier.",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID id,

        @Schema(
                description = "Exercise template name.",
                example = "Incline Dumbbell Press"
        )
        String name,

        @Schema(
                description = "Optional notes or execution guidance.",
                example = "Pause briefly at the bottom of each repetition.",
                nullable = true
        )
        String notes,

        @Schema(
                description = "Planned number of sets.",
                example = "3"
        )
        int setsCount,

        @Schema(
                description = "Planned number of reps.",
                example = "8-12",
                nullable = true
        )
        String reps,

        @Schema(
                description = "Rest time between sets, expressed in seconds.",
                example = "120"
        )
        Integer restBetweenSetsSeconds,

        @Schema(
                description = "Rest time after completing this exercise before moving to the next exercise, expressed in seconds.",
                example = "180"
        )
        Integer restAfterExerciseSeconds,

        @Schema(
                description = "Order of the exercise inside the workout template.",
                example = "1"
        )
        int orderIndex
) {
}