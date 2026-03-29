package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.model.ExerciseSession;
import com.krzywdek19.workout_service.model.SetSession;
import com.krzywdek19.workout_service.model.dto.SetSessionDto;
import com.krzywdek19.workout_service.model.request.UpdateSetRequest;
import com.krzywdek19.workout_service.repository.SetSessionRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.service.CurrentUserService;
import com.krzywdek19.workout_service.service.SetSessionService;
import com.krzywdek19.workout_service.utils.SetSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SetSessionServiceImpl implements SetSessionService {
    private final SetSessionRepository setSessionRepository;
    private final AuthorizationService authorizationService;
    private final SetSessionMapper setSessionMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public List<SetSessionDto> getSetSessionsForExercise(UUID exerciseSessionId) {
        String userEmail = currentUserService.getCurrentUserEmail();
        ExerciseSession exerciseSession =
                authorizationService.verifyAndGetExerciseSession(exerciseSessionId, userEmail);

        return setSessionRepository.findAllByExerciseSessionIdOrderByOrderIndexAsc(exerciseSession.getId())
                .stream()
                .map(setSessionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SetSessionDto getSetSessionById(UUID setSessionId) {
        String userEmail = currentUserService.getCurrentUserEmail();
        SetSession setSession = authorizationService.verifyAndGetSetSession(setSessionId, userEmail);
        return setSessionMapper.toDto(setSession);
    }

    @Override
    @Transactional
    public SetSessionDto updateSetSession(UUID setSessionId, UpdateSetRequest request) {
        String userEmail = currentUserService.getCurrentUserEmail();
        SetSession setSession = authorizationService.verifyAndGetSetSession(setSessionId, userEmail);

        setSession.setReps(request.reps());
        setSession.setWeight(request.weight());
        setSession.setRir(request.rir());
        setSession.setCompleted(request.completed());

        SetSession saved = setSessionRepository.save(setSession);
        return setSessionMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteSetSession(UUID setSessionId) {
        String userEmail = currentUserService.getCurrentUserEmail();
        SetSession setSession = authorizationService.verifyAndGetSetSession(setSessionId, userEmail);
        setSessionRepository.delete(setSession);
    }
}