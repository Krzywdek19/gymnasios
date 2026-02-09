package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.WorkoutTemplate;

import java.util.UUID;

public interface AuthorizationService {
    TrainingPlan verifyAndGetPlan(UUID planId, String userEmail);
    WorkoutTemplate verifyAndGetWorkoutTemplate(UUID templateId, String userEmail);
}
