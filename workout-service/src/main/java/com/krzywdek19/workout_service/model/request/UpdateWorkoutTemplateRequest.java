package com.krzywdek19.workout_service.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "UpdateWorkoutTemplateRequest",
        description = "Request payload used to update a workout template."
)
public record UpdateWorkoutTemplateRequest(
        @Schema(
                description = "Updated workout template name.",
                example = "Pull Day B"
        )
        @NotBlank
        String name
) {
}