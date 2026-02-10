package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;

import java.util.List;
import java.util.UUID;


public interface WorkoutTemplateService {
    WorkoutTemplateDto addWorkoutTemplateToPlan(UUID planId, CreateWorkoutTemplateRequest request);
    List<WorkoutTemplateDto> getWorkoutTemplatesForPlan(UUID planId);
    WorkoutTemplateDto getWorkoutTemplateById(UUID templateId);
    void deleteWorkoutTemplate(UUID templateId);
}
