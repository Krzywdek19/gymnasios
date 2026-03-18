package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.exceptions.ResourceOwnershipException;
import com.krzywdek19.workout_service.model.*;
import com.krzywdek19.workout_service.repository.*;
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
    private final ExerciseTemplateRepository exerciseTemplateRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseSessionRepository exerciseSessionRepository;
    private final SetSessionRepository setSessionRepository;

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

    @Override
    @Transactional(readOnly = true)
    public ExerciseTemplate verifyAndGetExerciseTemplate(UUID exerciseTemplateId, String userEmail) {
        ExerciseTemplate template = exerciseTemplateRepository.findByIdWithWorkoutTemplateAndTrainingPlan(exerciseTemplateId)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseTemplate", exerciseTemplateId));
        if (!template.getWorkoutTemplate().getTrainingPlan().getUserEmail().equals(userEmail)) {
            throw new ResourceOwnershipException("ExerciseTemplate", exerciseTemplateId);
        }
        return template;
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutSession verifyAndGetWorkoutSession(UUID workoutSessionId, String userEmail) {
        WorkoutSession session = workoutSessionRepository.findById(workoutSessionId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutSession", workoutSessionId));

        if (!session.getUserEmail().equals(userEmail)) {
            throw new ResourceOwnershipException("WorkoutSession", workoutSessionId);
        }
        return session;
    }

    @Override
    @Transactional(readOnly = true)
    public ExerciseSession verifyAndGetExerciseSession(UUID exerciseSessionId, String userEmail) {
        ExerciseSession session = exerciseSessionRepository.findByIdWithWorkoutSession(exerciseSessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseSession", exerciseSessionId));

        if (!session.getWorkoutSession().getUserEmail().equals(userEmail)) {
            throw new ResourceOwnershipException("ExerciseSession", exerciseSessionId);
        }
        return session;
    }

    @Override
    @Transactional(readOnly = true)
    public SetSession verifyAndGetSetSession(UUID setSessionId, String userEmail) {
        SetSession setSession = setSessionRepository.findByIdWithExerciseSessionAndWorkoutSession(setSessionId)
                .orElseThrow(() -> new ResourceNotFoundException("SetSession", setSessionId));

        if (!setSession.getExerciseSession().getWorkoutSession().getUserEmail().equals(userEmail)) {
            throw new ResourceOwnershipException("SetSession", setSessionId);
        }
        return setSession;
    }
}