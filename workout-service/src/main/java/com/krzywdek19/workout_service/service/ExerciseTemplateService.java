package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.ExerciseTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateExerciseTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateExerciseTemplateRequest;

import java.util.List;
import java.util.UUID;

public interface ExerciseTemplateService {
    ExerciseTemplateDto addExerciseTemplate(UUID workoutTemplateId, CreateExerciseTemplateRequest request);
    ExerciseTemplateDto getExerciseTemplateById(UUID exerciseTemplateId);
    List<ExerciseTemplateDto> getExerciseTemplatesForWorkout(UUID workoutTemplateId);
    ExerciseTemplateDto updateExerciseTemplate(UUID exerciseTemplateId, UpdateExerciseTemplateRequest request);
    void deleteExerciseTemplate(UUID exerciseTemplateId);
}
