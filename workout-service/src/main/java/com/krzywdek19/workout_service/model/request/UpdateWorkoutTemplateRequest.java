package com.krzywdek19.workout_service.model.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateWorkoutTemplateRequest (@NotBlank String name){
}
