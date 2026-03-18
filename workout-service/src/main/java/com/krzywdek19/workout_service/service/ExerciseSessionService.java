package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.ExerciseSessionDto;

import java.util.List;
import java.util.UUID;

public interface ExerciseSessionService {

    List<ExerciseSessionDto> getExerciseSessionsForWorkout(UUID workoutSessionId);

    ExerciseSessionDto getExerciseSessionById(UUID exerciseSessionId);
}