package com.krzywdek19.workout_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(
        name = "TrainingPlanDto",
        description = "Training plan representation returned by the workout service."
)
public record TrainingPlanDto(
        @Schema(
                description = "Training plan identifier.",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID id,

        @Schema(
                description = "Training plan name.",
                example = "Upper / Lower Split"
        )
        String name,

        @Schema(
                description = "Current training plan status.",
                example = "ACTIVE"
        )
        String status,

        @Schema(
                description = "Workout templates assigned to the training plan."
        )
        List<WorkoutTemplateDto> workouts
) {
}