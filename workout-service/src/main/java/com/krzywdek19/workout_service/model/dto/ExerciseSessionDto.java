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
                description = "Snapshot of the exercise name copied from the exercise template when the workout session was started.",
                example = "Barbell Bench Press"
        )
        String name,

        @Schema(
                description = "Order of the exercise inside the workout session.",
                example = "0"
        )
        Integer orderIndex,

        @Schema(
                description = "Snapshot of the planned number of sets for this exercise.",
                example = "4"
        )
        Integer setsCount,

        @Schema(
                description = "Snapshot of the rest time between sets, expressed in seconds.",
                example = "120"
        )
        Integer restBetweenSetsSeconds,

        @Schema(
                description = "Snapshot of the rest time after completing this exercise before moving to the next exercise, expressed in seconds.",
                example = "180"
        )
        Integer restAfterExerciseSeconds,

        @Schema(
                description = "List of performed sets for the exercise session."
        )
        List<SetSessionDto> sets
) {
}