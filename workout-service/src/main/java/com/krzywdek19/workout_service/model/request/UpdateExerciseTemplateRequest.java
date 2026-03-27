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
                description = "Updated notes or execution tips for the exercise.",
                example = "Control the eccentric phase and avoid bouncing."
        )
        @NotBlank
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
                description = "Updated number of planned sets.",
                example = "3",
                minimum = "1"
        )
        @NotNull
        @Min(1)
        Integer setsCount
) {
}