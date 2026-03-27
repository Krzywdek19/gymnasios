package com.krzywdek19.workout_service.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "WorkoutSessionStatus",
        description = "Status of a workout session."
)
public enum WorkoutSessionStatus {

    @Schema(description = "The workout session is currently in progress.")
    IN_PROGRESS,

    @Schema(description = "The workout session was completed normally.")
    FINISHED,

    @Schema(description = "The workout session was abandoned before completion.")
    ABANDONED
}