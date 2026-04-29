package com.krzywdek19.workout_service.service.impl;


import com.krzywdek19.workout_service.model.WorkoutTemplate;
import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.repository.ExerciseSessionRepository;
import com.krzywdek19.workout_service.repository.WorkoutSessionRepository;
import com.krzywdek19.workout_service.repository.WorkoutTemplateRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.service.CurrentUserService;
import com.krzywdek19.workout_service.service.WorkoutTemplateService;
import com.krzywdek19.workout_service.utils.WorkoutTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WorkoutTemplateServiceImpl implements WorkoutTemplateService {
    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final WorkoutTemplateMapper workoutTemplateMapper;
    private final AuthorizationService authorizationService;
    private final CurrentUserService currentUserService;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseSessionRepository exerciseSessionRepository;

    @Override
    @Transactional
    public WorkoutTemplateDto addWorkoutTemplateToPlan(UUID planId, CreateWorkoutTemplateRequest request) {
        String userEmail = currentUserService.getCurrentUserEmail();
        var trainingPlan = authorizationService.verifyAndGetPlan(planId, userEmail);
        var workoutTemplate = WorkoutTemplate.builder()
                .name(request.name())
                .orderIndex(request.orderIndex())
                .trainingPlan(trainingPlan)
                .build();
        return workoutTemplateMapper.toDto(workoutTemplateRepository.save(workoutTemplate));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutTemplateDto> getWorkoutTemplatesForPlan(UUID planId) {
        String userEmail = currentUserService.getCurrentUserEmail();
        List<WorkoutTemplate> templates = workoutTemplateRepository.findAllByTrainingPlanIdAndUserEmail(planId, userEmail);
        return templates.stream().map(workoutTemplateMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutTemplateDto getWorkoutTemplateById(UUID templateId) {
        String userEmail = currentUserService.getCurrentUserEmail();
        var workoutTemplate = authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail);
        return workoutTemplateMapper.toDto(workoutTemplate);
    }

    @Override
    @Transactional
    public WorkoutTemplateDto updateWorkoutTemplate(UUID templateId, UpdateWorkoutTemplateRequest request) {
        String userEmail = currentUserService.getCurrentUserEmail();
        WorkoutTemplate workoutTemplate = authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail);

        updateNameIfChanged(workoutTemplate, request.name());
        updateOrderIfChanged(workoutTemplate, request.order());

        return workoutTemplateMapper.toDto(workoutTemplateRepository.save(workoutTemplate));
    }

    private void updateNameIfChanged(WorkoutTemplate workoutTemplate, String newName) {
        if (!newName.equals(workoutTemplate.getName())) {
            workoutTemplate.setName(newName);
        }
    }

    private void updateOrderIfChanged(WorkoutTemplate workoutTemplate, Integer newOrder) {
        Integer oldOrder = workoutTemplate.getOrderIndex();

        if (newOrder.equals(oldOrder)) {
            return;
        }

        UUID planId = workoutTemplate.getTrainingPlan().getId();

        validateOrderInRange(planId, newOrder);
        shiftOtherWorkouts(planId, oldOrder, newOrder);

        workoutTemplate.setOrderIndex(newOrder);
    }

    private void shiftOtherWorkouts(UUID planId, Integer oldOrder, Integer newOrder) {
        if (newOrder < oldOrder) {
            workoutTemplateRepository
                    .findByTrainingPlanIdAndOrderIndexBetween(planId, newOrder, oldOrder - 1)
                    .forEach(workout -> workout.setOrderIndex(workout.getOrderIndex() + 1));
        } else {
            workoutTemplateRepository
                    .findByTrainingPlanIdAndOrderIndexBetween(planId, oldOrder + 1, newOrder)
                    .forEach(workout -> workout.setOrderIndex(workout.getOrderIndex() - 1));
        }
    }

    private void validateOrderInRange(UUID planId, Integer newOrder) {
        long workoutsCount = workoutTemplateRepository.countByTrainingPlanId(planId);

        if (newOrder < 1 || newOrder > workoutsCount) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Workout order must be between 1 and " + workoutsCount
            );
        }
    }

    @Override
    @Transactional
    public void deleteWorkoutTemplate(UUID workoutTemplateId) {
        String userEmail = currentUserService.getCurrentUserEmail();

        WorkoutTemplate workoutTemplate = authorizationService.verifyAndGetWorkoutTemplate(
                workoutTemplateId,
                userEmail
        );

        exerciseSessionRepository.detachExerciseTemplateReferencesByWorkoutTemplateId(workoutTemplateId);
        workoutSessionRepository.detachWorkoutTemplateReferencesByWorkoutTemplateId(workoutTemplateId);

        workoutTemplateRepository.delete(workoutTemplate);
    }
}
