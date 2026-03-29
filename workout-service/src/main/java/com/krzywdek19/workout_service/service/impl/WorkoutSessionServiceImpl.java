package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ActiveWorkoutSessionException;
import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.model.ExerciseSession;
import com.krzywdek19.workout_service.model.ExerciseTemplate;
import com.krzywdek19.workout_service.model.SetSession;
import com.krzywdek19.workout_service.model.WorkoutSession;
import com.krzywdek19.workout_service.model.WorkoutTemplate;
import com.krzywdek19.workout_service.model.dto.WorkoutSessionDto;
import com.krzywdek19.workout_service.model.enums.WorkoutSessionStatus;
import com.krzywdek19.workout_service.model.request.StartWorkoutSessionRequest;
import com.krzywdek19.workout_service.repository.WorkoutSessionRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.service.CurrentUserService;
import com.krzywdek19.workout_service.service.WorkoutSessionService;
import com.krzywdek19.workout_service.utils.WorkoutSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutSessionServiceImpl implements WorkoutSessionService {
    private final WorkoutSessionRepository workoutSessionRepository;
    private final AuthorizationService authorizationService;
    private final WorkoutSessionMapper workoutSessionMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public WorkoutSessionDto startWorkoutSession(StartWorkoutSessionRequest request) {
        String userEmail = currentUserService.getCurrentUserEmail();
        workoutSessionRepository.findByUserEmailAndStatus(userEmail, WorkoutSessionStatus.IN_PROGRESS)
                .ifPresent(session -> {
                    throw new ActiveWorkoutSessionException("User already has an active workout session.");
                });

        WorkoutTemplate workoutTemplate =
                authorizationService.verifyAndGetWorkoutTemplate(request.workoutTemplateId(), userEmail);

        WorkoutSession workoutSession = WorkoutSession.builder()
                .workoutTemplate(workoutTemplate)
                .userEmail(userEmail)
                .status(WorkoutSessionStatus.IN_PROGRESS)
                .startedAt(Instant.now())
                .exercises(new ArrayList<>())
                .build();

        List<ExerciseTemplate> sortedExerciseTemplates = workoutTemplate.getExercises()
                .stream()
                .sorted(Comparator.comparingInt(ExerciseTemplate::getOrderIndex))
                .toList();

        List<ExerciseSession> exerciseSessions = new ArrayList<>();

        for (ExerciseTemplate exerciseTemplate : sortedExerciseTemplates) {
            ExerciseSession exerciseSession = ExerciseSession.builder()
                    .workoutSession(workoutSession)
                    .exerciseTemplate(exerciseTemplate)
                    .name(exerciseTemplate.getName())
                    .orderIndex(exerciseTemplate.getOrderIndex())
                    .setsCount(exerciseTemplate.getSetsCount())
                    .sets(new ArrayList<>())
                    .build();

            List<SetSession> setSessions = new ArrayList<>();
            for (int i = 1; i <= exerciseTemplate.getSetsCount(); i++) {
                SetSession setSession = SetSession.builder()
                        .exerciseSession(exerciseSession)
                        .orderIndex(i)
                        .completed(false)
                        .build();
                setSessions.add(setSession);
            }

            exerciseSession.setSets(setSessions);
            exerciseSessions.add(exerciseSession);
        }

        workoutSession.setExercises(exerciseSessions);

        WorkoutSession saved = workoutSessionRepository.save(workoutSession);
        return workoutSessionMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutSessionDto getWorkoutSessionById(UUID workoutSessionId) {
        String userEmail = currentUserService.getCurrentUserEmail();
        WorkoutSession workoutSession = authorizationService.verifyAndGetWorkoutSession(workoutSessionId, userEmail);
        return workoutSessionMapper.toDto(workoutSession);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutSessionDto getActiveWorkoutSession() {
        String userEmail = currentUserService.getCurrentUserEmail();
        WorkoutSession workoutSession = workoutSessionRepository
                .findByUserEmailAndStatus(userEmail, WorkoutSessionStatus.IN_PROGRESS)
                .orElseThrow(() -> new ResourceNotFoundException("ActiveWorkoutSession", UUID.randomUUID()));

        return workoutSessionMapper.toDto(workoutSession);
    }

    @Override
    @Transactional
    public WorkoutSessionDto finishWorkoutSession(UUID workoutSessionId) {
        String userEmail = currentUserService.getCurrentUserEmail();
        WorkoutSession workoutSession = authorizationService.verifyAndGetWorkoutSession(workoutSessionId, userEmail);

        if (workoutSession.getStatus() != WorkoutSessionStatus.IN_PROGRESS) {
            return workoutSessionMapper.toDto(workoutSession);
        }

        workoutSession.setStatus(WorkoutSessionStatus.FINISHED);
        workoutSession.setFinishedAt(Instant.now());

        WorkoutSession saved = workoutSessionRepository.save(workoutSession);
        return workoutSessionMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutSessionDto> getAllUserWorkoutSessions() {
        String userEmail = currentUserService.getCurrentUserEmail();
        return workoutSessionRepository.findAllByUserEmailOrderByStartedAtDesc(userEmail)
                .stream()
                .map(workoutSessionMapper::toDto)
                .toList();
    }
}