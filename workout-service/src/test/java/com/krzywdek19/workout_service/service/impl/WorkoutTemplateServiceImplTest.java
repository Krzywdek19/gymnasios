package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.exceptions.ResourceOwnershipException;
import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.WorkoutTemplate;
import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.repository.ExerciseSessionRepository;
import com.krzywdek19.workout_service.repository.WorkoutSessionRepository;
import com.krzywdek19.workout_service.repository.WorkoutTemplateRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.service.CurrentUserService;
import com.krzywdek19.workout_service.utils.WorkoutTemplateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutTemplateServiceImplTest {

    @Mock
    private WorkoutTemplateRepository workoutTemplateRepository;

    @Mock
    private WorkoutTemplateMapper workoutTemplateMapper;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private WorkoutSessionRepository workoutSessionRepository;

    @Mock
    private ExerciseSessionRepository exerciseSessionRepository;

    private WorkoutTemplateServiceImpl workoutTemplateService;

    @BeforeEach
    void setUp() {
        workoutTemplateService = new WorkoutTemplateServiceImpl(
                workoutTemplateRepository,
                workoutTemplateMapper,
                authorizationService,
                currentUserService,
                workoutSessionRepository,
                exerciseSessionRepository
        );
    }

    @Test
    void addWorkoutTemplateToPlan_shouldCreateAndReturnTemplateDto() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";

        CreateWorkoutTemplateRequest request = new CreateWorkoutTemplateRequest(
                "Morning Workout",
                1
        );

        TrainingPlan plan = TrainingPlan.builder()
                .id(planId)
                .userEmail(userEmail)
                .build();

        WorkoutTemplate template = WorkoutTemplate.builder()
                .name(request.name())
                .orderIndex(request.orderIndex())
                .trainingPlan(plan)
                .build();

        WorkoutTemplateDto dto = new WorkoutTemplateDto(
                UUID.randomUUID(),
                "Morning Workout",
                1,
                new ArrayList<>()
        );

        when(currentUserService.getCurrentUserEmail()).thenReturn(userEmail);
        when(authorizationService.verifyAndGetPlan(planId, userEmail)).thenReturn(plan);
        when(workoutTemplateRepository.save(any(WorkoutTemplate.class))).thenReturn(template);
        when(workoutTemplateMapper.toDto(template)).thenReturn(dto);

        WorkoutTemplateDto result = workoutTemplateService.addWorkoutTemplateToPlan(planId, request);

        assertNotNull(result);
        assertEquals(dto.name(), result.name());

        verify(authorizationService).verifyAndGetPlan(planId, userEmail);
        verify(workoutTemplateRepository).save(any(WorkoutTemplate.class));
    }

    @Test
    void addWorkoutTemplateToPlan_shouldThrowException_whenPlanNotFound() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";

        CreateWorkoutTemplateRequest request = new CreateWorkoutTemplateRequest(
                "Workout",
                1
        );

        when(currentUserService.getCurrentUserEmail()).thenReturn(userEmail);
        when(authorizationService.verifyAndGetPlan(planId, userEmail))
                .thenThrow(new ResourceNotFoundException("TrainingPlan", planId));

        assertThrows(ResourceNotFoundException.class, () ->
                workoutTemplateService.addWorkoutTemplateToPlan(planId, request)
        );
    }

    @Test
    void getWorkoutTemplatesForPlan_shouldReturnListOfDtos() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";

        TrainingPlan plan = TrainingPlan.builder()
                .id(planId)
                .userEmail(userEmail)
                .build();

        WorkoutTemplate template = WorkoutTemplate.builder()
                .trainingPlan(plan)
                .build();

        WorkoutTemplateDto dto = new WorkoutTemplateDto(
                UUID.randomUUID(),
                "Workout",
                1,
                new ArrayList<>()
        );

        when(currentUserService.getCurrentUserEmail()).thenReturn(userEmail);
        when(workoutTemplateRepository.findAllByTrainingPlanIdAndUserEmail(planId, userEmail))
                .thenReturn(Collections.singletonList(template));
        when(workoutTemplateMapper.toDto(template)).thenReturn(dto);

        List<WorkoutTemplateDto> result = workoutTemplateService.getWorkoutTemplatesForPlan(planId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));

        verify(workoutTemplateRepository).findAllByTrainingPlanIdAndUserEmail(planId, userEmail);
        verify(workoutTemplateMapper).toDto(template);
    }

    @Test
    void getWorkoutTemplateById_shouldReturnDto_whenTemplateExists() {
        UUID templateId = UUID.randomUUID();
        String userEmail = "user@example.com";

        WorkoutTemplate template = WorkoutTemplate.builder()
                .id(templateId)
                .build();

        WorkoutTemplateDto dto = new WorkoutTemplateDto(
                templateId,
                "Workout",
                1,
                new ArrayList<>()
        );

        when(currentUserService.getCurrentUserEmail()).thenReturn(userEmail);
        when(authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail))
                .thenReturn(template);
        when(workoutTemplateMapper.toDto(template)).thenReturn(dto);

        WorkoutTemplateDto result = workoutTemplateService.getWorkoutTemplateById(templateId);

        assertNotNull(result);
        assertEquals(dto.id(), result.id());

        verify(authorizationService).verifyAndGetWorkoutTemplate(templateId, userEmail);
        verify(workoutTemplateMapper).toDto(template);
    }

    @Test
    void getWorkoutTemplateById_shouldThrowException_whenTemplateNotOwned() {
        UUID templateId = UUID.randomUUID();
        String userEmail = "user@example.com";

        when(currentUserService.getCurrentUserEmail()).thenReturn(userEmail);
        when(authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail))
                .thenThrow(new ResourceOwnershipException("WorkoutTemplate", templateId));

        assertThrows(ResourceOwnershipException.class, () ->
                workoutTemplateService.getWorkoutTemplateById(templateId)
        );
    }

    @Test
    void deleteWorkoutTemplate_shouldDetachSessionReferencesAndCallRepositoryDelete() {
        UUID templateId = UUID.randomUUID();
        String userEmail = "user@example.com";

        WorkoutTemplate template = WorkoutTemplate.builder()
                .id(templateId)
                .build();

        when(currentUserService.getCurrentUserEmail()).thenReturn(userEmail);
        when(authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail))
                .thenReturn(template);

        workoutTemplateService.deleteWorkoutTemplate(templateId);

        verify(authorizationService).verifyAndGetWorkoutTemplate(templateId, userEmail);
        verify(exerciseSessionRepository)
                .detachExerciseTemplateReferencesByWorkoutTemplateId(templateId);
        verify(workoutSessionRepository)
                .detachWorkoutTemplateReferencesByWorkoutTemplateId(templateId);
        verify(workoutTemplateRepository).delete(template);
    }

    @Test
    void deleteWorkoutTemplate_shouldThrowException_whenTemplateNotFound() {
        UUID templateId = UUID.randomUUID();
        String userEmail = "user@example.com";

        when(currentUserService.getCurrentUserEmail()).thenReturn(userEmail);
        when(authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail))
                .thenThrow(new ResourceNotFoundException("WorkoutTemplate", templateId));

        assertThrows(ResourceNotFoundException.class, () ->
                workoutTemplateService.deleteWorkoutTemplate(templateId)
        );
    }
}