package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.*;

import java.util.UUID;

public interface AuthorizationService {
    TrainingPlan verifyAndGetPlan(UUID planId, String userEmail);
    WorkoutTemplate verifyAndGetWorkoutTemplate(UUID templateId, String userEmail);
    ExerciseTemplate verifyAndGetExerciseTemplate(UUID exerciseTemplateId, String userEmail);
    WorkoutSession verifyAndGetWorkoutSession(UUID workoutSessionId, String userEmail);
    ExerciseSession verifyAndGetExerciseSession(UUID exerciseSessionId, String userEmail);
    SetSession verifyAndGetSetSession(UUID setSessionId, String userEmail);
}
