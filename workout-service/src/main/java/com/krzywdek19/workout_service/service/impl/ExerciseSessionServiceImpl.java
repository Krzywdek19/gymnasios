package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.model.ExerciseSession;
import com.krzywdek19.workout_service.model.WorkoutSession;
import com.krzywdek19.workout_service.model.dto.ExerciseSessionDto;
import com.krzywdek19.workout_service.repository.ExerciseSessionRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.service.ExerciseSessionService;
import com.krzywdek19.workout_service.utils.ExerciseSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseSessionServiceImpl implements ExerciseSessionService {

    private final ExerciseSessionRepository exerciseSessionRepository;
    private final AuthorizationService authorizationService;
    private final ExerciseSessionMapper exerciseSessionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseSessionDto> getExerciseSessionsForWorkout(UUID workoutSessionId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        WorkoutSession workoutSession =
                authorizationService.verifyAndGetWorkoutSession(workoutSessionId, userEmail);

        return exerciseSessionRepository.findAllByWorkoutSessionIdOrderByOrderIndexAsc(workoutSession.getId())
                .stream()
                .map(exerciseSessionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExerciseSessionDto getExerciseSessionById(UUID exerciseSessionId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        ExerciseSession exerciseSession =
                authorizationService.verifyAndGetExerciseSession(exerciseSessionId, userEmail);

        return exerciseSessionMapper.toDto(exerciseSession);
    }
}