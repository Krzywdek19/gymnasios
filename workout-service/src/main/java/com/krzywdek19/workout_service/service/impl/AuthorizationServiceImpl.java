package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.exceptions.ResourceOwnershipException;
import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.WorkoutTemplate;
import com.krzywdek19.workout_service.repository.TrainingPlanRepository;
import com.krzywdek19.workout_service.repository.WorkoutTemplateRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final WorkoutTemplateRepository workoutTemplateRepository;

    @Override
    @Transactional(readOnly = true)
    public TrainingPlan verifyAndGetPlan(UUID planId, String userEmail) {
        TrainingPlan plan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("TrainingPlan", planId));

        if (!plan.getUserEmail().equals(userEmail)) {
            throw new ResourceOwnershipException("TrainingPlan", planId);
        }
        return plan;
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutTemplate verifyAndGetWorkoutTemplate(UUID templateId, String userEmail) {
        WorkoutTemplate template = workoutTemplateRepository.findByIdWithTrainingPlan(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutTemplate", templateId));

        if (!template.getTrainingPlan().getUserEmail().equals(userEmail)) {
            throw new ResourceOwnershipException("WorkoutTemplate", templateId);
        }
        return template;
    }
}