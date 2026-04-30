package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.WorkoutSessionDto;
import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.StartWorkoutSessionRequest;

import java.util.List;
import java.util.UUID;

public interface WorkoutSessionService {

    WorkoutSessionDto startWorkoutSession(StartWorkoutSessionRequest request);

    WorkoutTemplateDto getNextWorkoutTemplate();

    WorkoutSessionDto startNextWorkoutSession();

    WorkoutSessionDto getWorkoutSessionById(UUID workoutSessionId);

    WorkoutSessionDto getActiveWorkoutSession();

    WorkoutSessionDto finishWorkoutSession(UUID workoutSessionId);

    List<WorkoutSessionDto> getAllUserWorkoutSessions();
    void deleteWorkoutSession(UUID workoutSessionId);

    void deleteFinishedWorkoutSessions();

    void deleteFinishedWorkoutSessionsByWorkoutTemplate(UUID workoutTemplateId);
}