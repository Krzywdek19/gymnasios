package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.model.ExerciseTemplate;
import com.krzywdek19.workout_service.model.dto.ExerciseTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateExerciseTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateExerciseTemplateRequest;
import com.krzywdek19.workout_service.repository.ExerciseSessionRepository;
import com.krzywdek19.workout_service.repository.ExerciseTemplateRepository;
import com.krzywdek19.workout_service.service.CurrentUserService;
import com.krzywdek19.workout_service.service.ExerciseTemplateService;
import com.krzywdek19.workout_service.utils.ExerciseTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseTemplateServiceImpl implements ExerciseTemplateService {
    private final ExerciseTemplateRepository exerciseTemplateRepository;
    private final ExerciseTemplateMapper exerciseTemplateMapper;
    private final AuthorizationServiceImpl authorizationService;
    private final CurrentUserService currentUserService;
    private final ExerciseSessionRepository exerciseSessionRepository;

    @Override
    @Transactional
    public ExerciseTemplateDto addExerciseTemplate(UUID workoutTemplateId, CreateExerciseTemplateRequest request) {
        String userEmail = currentUserService.getCurrentUserEmail();
        var workoutTemplate = authorizationService.verifyAndGetWorkoutTemplate(workoutTemplateId, userEmail);
        var exerciseTemplate = ExerciseTemplate.builder()
                .workoutTemplate(workoutTemplate)
                .name(request.name())
                .notes(request.notes())
                .orderIndex(request.orderIndex())
                .setsCount(request.setsCount())
                .restBetweenSetsSeconds(
                        request.restBetweenSetsSeconds() != null
                                ? request.restBetweenSetsSeconds()
                                : 120
                )
                .restAfterExerciseSeconds(
                        request.restAfterExerciseSeconds() != null
                                ? request.restAfterExerciseSeconds()
                                : 180
                )
                .reps(request.reps())
                .build();
        return exerciseTemplateMapper.toDto(exerciseTemplateRepository.save(exerciseTemplate));
    }

    @Override
    @Transactional(readOnly = true)
    public ExerciseTemplateDto getExerciseTemplateById(UUID exerciseTemplateId) {
        String userEmail = currentUserService.getCurrentUserEmail();
        ExerciseTemplate exerciseTemplate = authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, userEmail);
        return exerciseTemplateMapper.toDto(exerciseTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseTemplateDto> getExerciseTemplatesForWorkout(UUID workoutTemplateId) {
        String userEmail = currentUserService.getCurrentUserEmail();
        authorizationService.verifyAndGetWorkoutTemplate(workoutTemplateId, userEmail);
        return exerciseTemplateRepository.findAllByWorkoutTemplateId(workoutTemplateId)
                .stream()
                .map(exerciseTemplateMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ExerciseTemplateDto updateExerciseTemplate(UUID exerciseTemplateId, UpdateExerciseTemplateRequest request) {
        String userEmail = currentUserService.getCurrentUserEmail();
        var exerciseTemplate = authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, userEmail);
        exerciseTemplate.setName(request.name());
        exerciseTemplate.setOrderIndex(request.orderIndex());
        exerciseTemplate.setSetsCount(request.setsCount());
        exerciseTemplate.setNotes(request.notes());
        if (request.restBetweenSetsSeconds() != null) {
            exerciseTemplate.setRestBetweenSetsSeconds(request.restBetweenSetsSeconds());
        }

        if (request.restAfterExerciseSeconds() != null) {
            exerciseTemplate.setRestAfterExerciseSeconds(request.restAfterExerciseSeconds());
        }
        exerciseTemplate.setReps(request.reps());
        return exerciseTemplateMapper.toDto(exerciseTemplateRepository.save(exerciseTemplate));
    }

    @Override
    @Transactional
    public void deleteExerciseTemplate(UUID exerciseTemplateId) {
        String userEmail = currentUserService.getCurrentUserEmail();

        ExerciseTemplate exerciseTemplate = authorizationService.verifyAndGetExerciseTemplate(
                exerciseTemplateId,
                userEmail
        );

        exerciseSessionRepository.detachExerciseTemplateReferencesByExerciseTemplateId(exerciseTemplateId);

        exerciseTemplateRepository.delete(exerciseTemplate);
    }
}
