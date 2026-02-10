package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.model.ExerciseTemplate;
import com.krzywdek19.workout_service.model.dto.ExerciseTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateExerciseTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateExerciseTemplateRequest;
import com.krzywdek19.workout_service.repository.ExerciseTemplateRepository;
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

    @Override
    @Transactional
    public ExerciseTemplateDto addExerciseTemplate(UUID workoutTemplateId, CreateExerciseTemplateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var workoutTemplate = authorizationService.verifyAndGetWorkoutTemplate(workoutTemplateId, email);
        var exerciseTemplate = ExerciseTemplate.builder()
                .workoutTemplate(workoutTemplate)
                .name(request.name())
                .notes(request.notes())
                .orderIndex(request.orderIndex())
                .setsCount(request.setsCount())
                .build();
        return exerciseTemplateMapper.toDto(exerciseTemplateRepository.save(exerciseTemplate));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseTemplateDto> getExerciseTemplatesForWorkout(UUID workoutTemplateId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        authorizationService.verifyAndGetWorkoutTemplate(workoutTemplateId, email);
        return exerciseTemplateRepository.findAllByWorkoutTemplateId(workoutTemplateId)
                .stream()
                .map(exerciseTemplateMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ExerciseTemplateDto updateExerciseTemplate(UUID exerciseTemplateId, UpdateExerciseTemplateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var exerciseTemplate = authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, email);
        exerciseTemplate.setName(request.name());
        exerciseTemplate.setOrderIndex(request.orderIndex());
        exerciseTemplate.setSetsCount(request.setsCount());
        exerciseTemplate.setNotes(request.notes());
        return exerciseTemplateMapper.toDto(exerciseTemplateRepository.save(exerciseTemplate));
    }

    @Override
    @Transactional
    public void deleteExerciseTemplate(UUID exerciseTemplateId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var exerciseTemplate = authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, email);
        exerciseTemplateRepository.delete(exerciseTemplate);
    }
}
