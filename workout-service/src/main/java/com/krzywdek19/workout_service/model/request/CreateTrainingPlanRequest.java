package com.krzywdek19.workout_service.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "CreateTrainingPlanRequest",
        description = "Request payload used to create a training plan."
)
public record CreateTrainingPlanRequest(
        @Schema(
                description = "Training plan name.",
                example = "Upper / Lower Split"
        )
        @NotBlank
        String name
) {
}