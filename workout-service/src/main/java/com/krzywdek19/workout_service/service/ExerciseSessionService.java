package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.ExerciseSessionDto;
import java.util.List;


public interface ExerciseSessionService {
    List<ExerciseSessionDto> getExerciseSessionsForWorkout(Long workoutSessionId, String userId);
    ExerciseSessionDto getExerciseSessionById(Long exerciseSessionId, String userId);
}
