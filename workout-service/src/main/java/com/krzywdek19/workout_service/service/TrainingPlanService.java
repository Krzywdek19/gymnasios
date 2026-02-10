package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;

import java.util.List;
import java.util.UUID;

public interface TrainingPlanService {
    TrainingPlanDto createPlan(CreateTrainingPlanRequest request);
    List<TrainingPlanDto> getPlansForCurrentUser();
    TrainingPlanDto getPlanById(UUID planId);
    void deletePlan(UUID planId);
}