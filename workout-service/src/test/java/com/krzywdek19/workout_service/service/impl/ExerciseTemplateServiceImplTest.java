package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.model.ExerciseTemplate;
import com.krzywdek19.workout_service.model.WorkoutTemplate;
import com.krzywdek19.workout_service.model.dto.ExerciseTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateExerciseTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateExerciseTemplateRequest;
import com.krzywdek19.workout_service.repository.ExerciseSessionRepository;
import com.krzywdek19.workout_service.repository.ExerciseTemplateRepository;
import com.krzywdek19.workout_service.utils.ExerciseTemplateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseTemplateServiceImplTest {

    @Mock
    private ExerciseTemplateRepository exerciseTemplateRepository;

    @Mock
    private ExerciseSessionRepository exerciseSessionRepository;

    @Mock
    private ExerciseTemplateMapper exerciseTemplateMapper;

    @Mock
    private AuthorizationServiceImpl authorizationService;

    @InjectMocks
    private ExerciseTemplateServiceImpl exerciseTemplateService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void addExerciseTemplate_shouldCreateAndSaveTemplate_whenAuthorized() {
        UUID workoutTemplateId = UUID.randomUUID();
        String email = "user@example.com";

        var request = new CreateExerciseTemplateRequest(
                "Push-ups",
                "Keep core tight",
                3,
                "10-12",
                120,
                180,
                1
        );

        var workoutTemplate = new WorkoutTemplate();
        var exerciseTemplate = ExerciseTemplate.builder()
                .name(request.name())
                .notes(request.notes())
                .setsCount(request.setsCount())
                .reps(request.reps())
                .restBetweenSetsSeconds(request.restBetweenSetsSeconds())
                .restAfterExerciseSeconds(request.restAfterExerciseSeconds())
                .orderIndex(request.orderIndex())
                .build();

        var expectedDto = new ExerciseTemplateDto(
                UUID.randomUUID(),
                request.name(),
                request.notes(),
                request.setsCount(),
                request.reps(),
                request.restBetweenSetsSeconds(),
                request.restAfterExerciseSeconds(),
                request.orderIndex()
        );

        when(authentication.getName()).thenReturn(email);
        when(authorizationService.verifyAndGetWorkoutTemplate(workoutTemplateId, email))
                .thenReturn(workoutTemplate);
        when(exerciseTemplateRepository.save(any(ExerciseTemplate.class)))
                .thenReturn(exerciseTemplate);
        when(exerciseTemplateMapper.toDto(exerciseTemplate))
                .thenReturn(expectedDto);

        ExerciseTemplateDto result = exerciseTemplateService.addExerciseTemplate(
                workoutTemplateId,
                request
        );

        assertThat(result).isEqualTo(expectedDto);
        verify(authorizationService).verifyAndGetWorkoutTemplate(workoutTemplateId, email);
        verify(exerciseTemplateRepository).save(any(ExerciseTemplate.class));
    }

    @Test
    void getExerciseTemplatesForWorkout_shouldReturnListOfDtos() {
        UUID workoutTemplateId = UUID.randomUUID();
        String email = "user@example.com";

        var workoutTemplate = new WorkoutTemplate();
        var exerciseTemplate = new ExerciseTemplate();

        var expectedDto = new ExerciseTemplateDto(
                UUID.randomUUID(),
                "Push-ups",
                "Keep core tight",
                3,
                "10-12",
                120,
                180,
                1
        );

        when(authentication.getName()).thenReturn(email);
        when(authorizationService.verifyAndGetWorkoutTemplate(workoutTemplateId, email))
                .thenReturn(workoutTemplate);
        when(exerciseTemplateRepository.findAllByWorkoutTemplateId(workoutTemplateId))
                .thenReturn(Collections.singletonList(exerciseTemplate));
        when(exerciseTemplateMapper.toDto(exerciseTemplate))
                .thenReturn(expectedDto);

        List<ExerciseTemplateDto> result = exerciseTemplateService
                .getExerciseTemplatesForWorkout(workoutTemplateId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(expectedDto);
    }

    @Test
    void updateExerciseTemplate_shouldUpdateAndReturnDto() {
        UUID exerciseTemplateId = UUID.randomUUID();
        String email = "user@example.com";

        var request = new UpdateExerciseTemplateRequest(
                "New Name",
                "New Notes",
                2,
                "8-12",
                90,
                150,
                4
        );

        var exerciseTemplate = new ExerciseTemplate();

        var expectedDto = new ExerciseTemplateDto(
                exerciseTemplateId,
                request.name(),
                request.notes(),
                request.setsCount(),
                request.reps(),
                request.restBetweenSetsSeconds(),
                request.restAfterExerciseSeconds(),
                request.orderIndex()
        );

        when(authentication.getName()).thenReturn(email);
        when(authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, email))
                .thenReturn(exerciseTemplate);
        when(exerciseTemplateRepository.save(any(ExerciseTemplate.class)))
                .thenReturn(exerciseTemplate);
        when(exerciseTemplateMapper.toDto(exerciseTemplate))
                .thenReturn(expectedDto);

        ExerciseTemplateDto result = exerciseTemplateService.updateExerciseTemplate(
                exerciseTemplateId,
                request
        );

        assertThat(result).isEqualTo(expectedDto);
        verify(authorizationService).verifyAndGetExerciseTemplate(exerciseTemplateId, email);
        verify(exerciseTemplateRepository).save(exerciseTemplate);
    }

    @Test
    void deleteExerciseTemplate_shouldDetachHistoryReferencesAndCallRepositoryDelete() {
        UUID exerciseTemplateId = UUID.randomUUID();
        String email = "user@example.com";

        var exerciseTemplate = new ExerciseTemplate();

        when(authentication.getName()).thenReturn(email);
        when(authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, email))
                .thenReturn(exerciseTemplate);

        exerciseTemplateService.deleteExerciseTemplate(exerciseTemplateId);

        verify(authorizationService).verifyAndGetExerciseTemplate(exerciseTemplateId, email);
        verify(exerciseSessionRepository)
                .detachExerciseTemplateReferencesByExerciseTemplateId(exerciseTemplateId);
        verify(exerciseTemplateRepository).delete(exerciseTemplate);
    }
}