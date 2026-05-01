package com.krzywdek19.workout_service.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "UpdateExerciseTemplateRequest",
        description = "Request payload used to update an existing exercise template."
)
public record UpdateExerciseTemplateRequest(
        @Schema(
                description = "Updated exercise template name.",
                example = "Incline Dumbbell Press"
        )
        @NotBlank
        String name,

        @Schema(
                description = "Optional notes or execution tips for the exercise.",
                example = "Control the eccentric phase and avoid bouncing.",
                nullable = true
        )
        String notes,

        @Schema(
                description = "Updated exercise order inside the workout template.",
                example = "1",
                minimum = "0"
        )
        @NotNull
        @Min(0)
        Integer orderIndex,

        @Schema(
                description = "Number of reps planned for the exercise.",
                example = "8-12"
        )
        String reps,

        @Schema(
                description = "Rest time between sets, expressed in seconds. If null, the existing value can be kept by the service.",
                example = "120",
                minimum = "0",
                nullable = true
        )
        @Min(0)
        Integer restBetweenSetsSeconds,

        @Schema(
                description = "Rest time after completing this exercise before moving to the next exercise, expressed in seconds. If null, the existing value can be kept by the service.",
                example = "180",
                minimum = "0",
                nullable = true
        )
        @Min(0)
        Integer restAfterExerciseSeconds,

        @Schema(
                description = "Updated number of planned sets.",
                example = "3",
                minimum = "1"
        )
        @NotNull
        @Min(1)
        Integer setsCount
) {
}