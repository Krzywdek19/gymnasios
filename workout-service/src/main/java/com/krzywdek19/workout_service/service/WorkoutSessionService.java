package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.WorkoutSessionDto;
import com.krzywdek19.workout_service.model.request.StartWorkoutSessionRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface WorkoutSessionService {
    WorkoutSessionDto startWorkoutSession(String userId, StartWorkoutSessionRequest request);
    WorkoutSessionDto getActiveWorkoutSession(String userId);
    WorkoutSessionDto finishWorkoutSession(String userId);
    List<WorkoutSessionDto> getAllUserWorkoutSessions(String userId);
}