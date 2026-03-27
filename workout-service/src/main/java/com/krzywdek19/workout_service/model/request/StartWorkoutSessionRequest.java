package com.krzywdek19.workout_service.model.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(
        name = "StartWorkoutSessionRequest",
        description = "Request payload used to start a workout session from an existing workout template."
)
public record StartWorkoutSessionRequest(
        @Schema(
                description = "Identifier of the workout template used to start the session.",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID workoutTemplateId
) {
}