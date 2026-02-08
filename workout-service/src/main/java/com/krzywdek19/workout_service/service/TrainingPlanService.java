package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;

import java.util.List;
import java.util.UUID;

public interface TrainingPlanService {
    TrainingPlanDto createPlan(String userEmail, CreateTrainingPlanRequest request);
    List<TrainingPlanDto> getPlansByUserEmail(String userEmail);
    TrainingPlanDto getPlanById(UUID planId, String userEmail);
    void deletePlan(UUID planId, String userEmail);
}