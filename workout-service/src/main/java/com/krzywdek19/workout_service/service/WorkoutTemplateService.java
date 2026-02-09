package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;

import java.util.List;
import java.util.UUID;


public interface WorkoutTemplateService {
    WorkoutTemplateDto addWorkoutTemplateToPlan(UUID planId, String userEmail, CreateWorkoutTemplateRequest request);
    List<WorkoutTemplateDto> getWorkoutTemplatesForPlan(UUID planId, String userEmail);
    WorkoutTemplateDto getWorkoutTemplateById(UUID templateId, String userEmail);
    void deleteWorkoutTemplate(UUID templateId, String userEmail);
}
