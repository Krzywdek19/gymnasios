package com.krzywdek19.workout_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(
        name = "WorkoutSessionDto",
        description = "Workout session representation returned by the workout service."
)
public record WorkoutSessionDto(
        @Schema(
                description = "Workout session identifier.",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID id,

        @Schema(
                description = "Identifier of the workout template used to create the session.",
                example = "7fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID workoutTemplateId,

        @Schema(
                description = "Current workout session status.",
                example = "IN_PROGRESS"
        )
        String status,

        @Schema(
                description = "Timestamp when the workout session started.",
                example = "2026-03-25T18:30:00Z"
        )
        Instant startedAt,

        @Schema(
                description = "Timestamp when the workout session finished. Null when the session is still active.",
                example = "2026-03-25T19:22:10Z",
                nullable = true
        )
        Instant finishedAt,

        @Schema(
                description = "Exercise sessions created inside this workout session."
        )
        List<ExerciseSessionDto> exercises
) {
}