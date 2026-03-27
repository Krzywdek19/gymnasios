package com.krzywdek19.workout_service.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "CreateWorkoutTemplateRequest",
        description = "Request payload used to create a workout template."
)
public record CreateWorkoutTemplateRequest(
        @Schema(
                description = "Workout template name.",
                example = "Push Day A"
        )
        @NotBlank
        String name,

        @Schema(
                description = "Order of the workout inside the training plan.",
                example = "0",
                minimum = "0"
        )
        @NotNull
        @Min(0)
        Integer orderIndex
) {
}