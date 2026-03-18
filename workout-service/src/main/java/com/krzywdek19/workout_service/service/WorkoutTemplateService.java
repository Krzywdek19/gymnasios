package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateWorkoutTemplateRequest;

import java.util.List;
import java.util.UUID;


public interface WorkoutTemplateService {
    WorkoutTemplateDto addWorkoutTemplateToPlan(UUID planId, CreateWorkoutTemplateRequest request);
    List<WorkoutTemplateDto> getWorkoutTemplatesForPlan(UUID planId);
    WorkoutTemplateDto getWorkoutTemplateById(UUID templateId);
    WorkoutTemplateDto updateWorkoutTemplate(UUID templateId, UpdateWorkoutTemplateRequest request);
    void deleteWorkoutTemplate(UUID templateId);
}
