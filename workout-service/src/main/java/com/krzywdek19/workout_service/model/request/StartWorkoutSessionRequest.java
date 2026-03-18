package com.krzywdek19.workout_service.model.request;


import java.util.UUID;

public record StartWorkoutSessionRequest(UUID workoutTemplateId){
}
