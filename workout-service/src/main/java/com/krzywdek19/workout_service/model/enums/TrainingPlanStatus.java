package com.krzywdek19.workout_service.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "TrainingPlanStatus",
        description = "Status of a training plan."
)
public enum TrainingPlanStatus {

    @Schema(description = "The training plan is selected as the current active plan.")
    ACTIVE,

    @Schema(description = "The training plan exists, but is not currently selected as active.")
    INACTIVE,

    @Schema(description = "The training plan is archived and no longer actively used.")
    ARCHIVED
}