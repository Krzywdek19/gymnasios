package com.krzywdek19.workout_service.service.impl;


import com.krzywdek19.workout_service.model.WorkoutTemplate;
import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.repository.WorkoutTemplateRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.service.WorkoutTemplateService;
import com.krzywdek19.workout_service.utils.WorkoutTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WorkoutTemplateServiceImpl implements WorkoutTemplateService {
    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final WorkoutTemplateMapper workoutTemplateMapper;
    private final AuthorizationService authorizationService;

    @Override
    @Transactional
    public WorkoutTemplateDto addWorkoutTemplateToPlan(UUID planId, String userEmail, CreateWorkoutTemplateRequest request) {
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
    public List<WorkoutTemplateDto> getWorkoutTemplatesForPlan(UUID planId, String userEmail) {
        authorizationService.verifyAndGetPlan(planId, userEmail);
        List<WorkoutTemplate> templates = workoutTemplateRepository.findAllByTrainingPlanIdAndUserEmail(planId, userEmail);
        return templates.stream().map(workoutTemplateMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutTemplateDto getWorkoutTemplateById(UUID templateId, String userEmail) {
        var workoutTemplate = authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail);
        return workoutTemplateMapper.toDto(workoutTemplate);
    }

    @Override
    @Transactional
    public void deleteWorkoutTemplate(UUID templateId, String userEmail) {
        var workoutTemplate = authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail);
        workoutTemplateRepository.delete(workoutTemplate);
    }
}
