package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;

import java.util.List;


public interface WorkoutTemplateService {
    WorkoutTemplateDto addWorkoutTemplateToPlan(Long planId, String userId, CreateWorkoutTemplateRequest request);
    List<WorkoutTemplateDto> getWorkoutTemplatesForPlan(Long planId, String userId);
    WorkoutTemplateDto getWorkoutTemplateById(Long templateId, String userId);
    void deleteWorkoutTemplate(Long templateId, String userId);
}
