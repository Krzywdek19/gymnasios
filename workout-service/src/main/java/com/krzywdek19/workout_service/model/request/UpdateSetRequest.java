package com.krzywdek19.workout_service.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(
        name = "UpdateSetRequest",
        description = "Request payload used to update a single set inside an exercise session."
)
public record UpdateSetRequest(
        @Schema(
                description = "Number of repetitions performed.",
                example = "8",
                minimum = "0"
        )
        @NotNull
        @Min(0)
        Integer reps,

        @Schema(
                description = "Weight used in the set.",
                example = "80.0",
                minimum = "0.0"
        )
        @NotNull
        @DecimalMin("0.0")
        BigDecimal weight,

        @Schema(
                description = "Reps in reserve for the set. Can be null when not tracked.",
                example = "2",
                nullable = true
        )
        Integer rir,

        @Schema(
                description = "Indicates whether the set has been completed.",
                example = "true"
        )
        @NotNull
        Boolean completed
) {
}