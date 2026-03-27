package com.krzywdek19.workout_service.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "UpdateTrainingPlanRequest",
        description = "Request payload used to update a training plan."
)
public record UpdateTrainingPlanRequest(
        @Schema(
                description = "Updated training plan name.",
                example = "Push Pull Legs"
        )
        @NotBlank
        String name
) {
}