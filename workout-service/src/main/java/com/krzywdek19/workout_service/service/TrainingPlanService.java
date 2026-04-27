package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;
import com.krzywdek19.workout_service.model.request.UpdateTrainingPlanRequest;

import java.util.List;
import java.util.UUID;

public interface TrainingPlanService {
    TrainingPlanDto createPlan(CreateTrainingPlanRequest request);

    List<TrainingPlanDto> getPlansForCurrentUser();

    TrainingPlanDto getPlanById(UUID planId);

    TrainingPlanDto getActivePlan();

    TrainingPlanDto activatePlan(UUID planId);

    TrainingPlanDto updatePlan(UUID planId, UpdateTrainingPlanRequest request);

    void deletePlan(UUID planId);
}