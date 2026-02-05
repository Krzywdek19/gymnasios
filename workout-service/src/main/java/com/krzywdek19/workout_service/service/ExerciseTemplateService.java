package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.ExerciseTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateExerciseTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateExerciseTemplateRequest;

import java.util.List;

public interface ExerciseTemplateService {
    ExerciseTemplateDto addExerciseTemplate(Long workoutTemplateId, String userId, CreateExerciseTemplateRequest request);
    List<ExerciseTemplateDto> getExerciseTemplatesForWorkout(Long workoutTemplateId, String userId);
    ExerciseTemplateDto updateExerciseTemplate(Long exerciseTemplateId, String userId, UpdateExerciseTemplateRequest request);
    void deleteExerciseTemplate(Long exerciseTemplateId, String userId);
}