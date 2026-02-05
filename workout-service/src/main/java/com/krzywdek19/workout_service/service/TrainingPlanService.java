package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;

import java.util.List;
import java.util.UUID;

public interface TrainingPlanService {
    TrainingPlanDto createPlan(String userEmail, CreateTrainingPlanRequest request);
    List<TrainingPlanDto> getPlansByUserId(String userId);
    TrainingPlanDto getPlanById(Long planId, String userId);
    void deletePlan(Long planId, String userId);
}