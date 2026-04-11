package com.krzywdek19.workout_service.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "CreateExerciseTemplateRequest",
        description = "Request payload used to create an exercise template."
)
public record CreateExerciseTemplateRequest(
        @Schema(
                description = "Exercise template name.",
                example = "Barbell Bench Press"
        )
        @NotBlank
        String name,

        @Schema(
                description = "Optional notes or execution tips for the exercise.",
                example = "Keep shoulder blades retracted and maintain a stable arch."
        )
        String notes,

        @Schema(
                description = "Number of sets planned for the exercise.",
                example = "4",
                minimum = "1"
        )
        @NotNull
        @Min(1)
        Integer setsCount,

        @Schema(
                description = "Number of reps planned for the exercise.",
                example = "8-12"
        )
        String reps,

        @Schema(
                description = "Order of the exercise inside the workout template.",
                example = "0",
                minimum = "0"
        )
        @NotNull
        @Min(0)
        Integer orderIndex
) {
}