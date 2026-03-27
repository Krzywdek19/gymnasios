package com.krzywdek19.workout_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(
        name = "WorkoutTemplateDto",
        description = "Workout template representation returned by the workout service."
)
public record WorkoutTemplateDto(
        @Schema(
                description = "Workout template identifier.",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID id,

        @Schema(
                description = "Workout template name.",
                example = "Push Day A"
        )
        String name,

        @Schema(
                description = "Order of the workout inside the training plan.",
                example = "0"
        )
        Integer orderIndex,

        @Schema(
                description = "Exercise templates assigned to the workout template."
        )
        List<ExerciseTemplateDto> exercises
) {
}