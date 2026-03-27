package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.exceptions.ResourceOwnershipException;
import com.krzywdek19.workout_service.model.ExerciseTemplate;
import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.WorkoutTemplate;
import com.krzywdek19.workout_service.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceImplTest {

    @Mock
    private TrainingPlanRepository trainingPlanRepository;

    @Mock
    private WorkoutTemplateRepository workoutTemplateRepository;

    @Mock
    private ExerciseTemplateRepository exerciseTemplateRepository;

    @Mock
    private WorkoutSessionRepository workoutSessionRepository;

    @Mock
    private ExerciseSessionRepository exerciseSessionRepository;

    @Mock
    private SetSessionRepository setSessionRepository;

    private AuthorizationServiceImpl authorizationService;

    @BeforeEach
    void setUp() {
        authorizationService = new AuthorizationServiceImpl(
                trainingPlanRepository,
                workoutTemplateRepository,
                exerciseTemplateRepository,
                workoutSessionRepository,
                exerciseSessionRepository,
                setSessionRepository
        );
    }

    //region verifyAndGetPlan Tests
    @Test
    void verifyAndGetPlan_shouldReturnPlan_whenPlanExistsAndUserIsOwner() {
        // Arrange
        UUID planId = UUID.randomUUID();
        String userEmail = "owner@example.com";
        TrainingPlan plan = TrainingPlan.builder().id(planId).userEmail(userEmail).build();

        when(trainingPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        // Act
        TrainingPlan result = authorizationService.verifyAndGetPlan(planId, userEmail);

        // Assert
        assertNotNull(result);
        assertEquals(planId, result.getId());
    }

    @Test
    void verifyAndGetPlan_shouldThrowResourceNotFoundException_whenPlanDoesNotExist() {
        // Arrange
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";

        when(trainingPlanRepository.findById(planId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            authorizationService.verifyAndGetPlan(planId, userEmail);
        });
    }

    @Test
    void verifyAndGetPlan_shouldThrowResourceOwnershipException_whenUserIsNotOwner() {
        // Arrange
        UUID planId = UUID.randomUUID();
        String ownerEmail = "owner@example.com";
        String requesterEmail = "requester@example.com";
        TrainingPlan plan = TrainingPlan.builder().id(planId).userEmail(ownerEmail).build();

        when(trainingPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        // Act & Assert
        assertThrows(ResourceOwnershipException.class, () -> {
            authorizationService.verifyAndGetPlan(planId, requesterEmail);
        });
    }
    //endregion

    //region verifyAndGetWorkoutTemplate Tests
    @Test
    void verifyAndGetWorkoutTemplate_shouldReturnTemplate_whenTemplateExistsAndUserIsOwner() {
        // Arrange
        UUID templateId = UUID.randomUUID();
        String userEmail = "owner@example.com";
        TrainingPlan plan = TrainingPlan.builder().userEmail(userEmail).build();
        WorkoutTemplate template = WorkoutTemplate.builder().id(templateId).trainingPlan(plan).build();

        when(workoutTemplateRepository.findByIdWithTrainingPlan(templateId)).thenReturn(Optional.of(template));

        // Act
        WorkoutTemplate result = authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail);

        // Assert
        assertNotNull(result);
        assertEquals(templateId, result.getId());
    }

    @Test
    void verifyAndGetWorkoutTemplate_shouldThrowResourceNotFoundException_whenTemplateDoesNotExist() {
        // Arrange
        UUID templateId = UUID.randomUUID();
        String userEmail = "user@example.com";

        when(workoutTemplateRepository.findByIdWithTrainingPlan(templateId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            authorizationService.verifyAndGetWorkoutTemplate(templateId, userEmail);
        });
    }

    @Test
    void verifyAndGetWorkoutTemplate_shouldThrowResourceOwnershipException_whenUserIsNotOwner() {
        // Arrange
        UUID templateId = UUID.randomUUID();
        String ownerEmail = "owner@example.com";
        String requesterEmail = "requester@example.com";
        TrainingPlan plan = TrainingPlan.builder().userEmail(ownerEmail).build();
        WorkoutTemplate template = WorkoutTemplate.builder().id(templateId).trainingPlan(plan).build();

        when(workoutTemplateRepository.findByIdWithTrainingPlan(templateId)).thenReturn(Optional.of(template));

        // Act & Assert
        assertThrows(ResourceOwnershipException.class, () -> {
            authorizationService.verifyAndGetWorkoutTemplate(templateId, requesterEmail);
        });
    }
    //endregion

    //region verifyAndGetExerciseTemplate Tests
    @Test
    void verifyAndGetExerciseTemplate_shouldReturnTemplate_whenTemplateExistsAndUserIsOwner() {
        // Arrange
        UUID exerciseTemplateId = UUID.randomUUID();
        String userEmail = "owner@example.com";
        TrainingPlan plan = TrainingPlan.builder().userEmail(userEmail).build();
        WorkoutTemplate workoutTemplate = WorkoutTemplate.builder().trainingPlan(plan).build();
        ExerciseTemplate exerciseTemplate = ExerciseTemplate.builder().id(exerciseTemplateId).workoutTemplate(workoutTemplate).build();

        when(exerciseTemplateRepository.findByIdWithWorkoutTemplateAndTrainingPlan(exerciseTemplateId)).thenReturn(Optional.of(exerciseTemplate));

        // Act
        ExerciseTemplate result = authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, userEmail);

        // Assert
        assertNotNull(result);
        assertEquals(exerciseTemplateId, result.getId());
    }

    @Test
    void verifyAndGetExerciseTemplate_shouldThrowResourceNotFoundException_whenTemplateDoesNotExist() {
        // Arrange
        UUID exerciseTemplateId = UUID.randomUUID();
        String userEmail = "user@example.com";

        when(exerciseTemplateRepository.findByIdWithWorkoutTemplateAndTrainingPlan(exerciseTemplateId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, userEmail);
        });
    }

    @Test
    void verifyAndGetExerciseTemplate_shouldThrowResourceOwnershipException_whenUserIsNotOwner() {
        // Arrange
        UUID exerciseTemplateId = UUID.randomUUID();
        String ownerEmail = "owner@example.com";
        String requesterEmail = "requester@example.com";
        TrainingPlan plan = TrainingPlan.builder().userEmail(ownerEmail).build();
        WorkoutTemplate workoutTemplate = WorkoutTemplate.builder().trainingPlan(plan).build();
        ExerciseTemplate exerciseTemplate = ExerciseTemplate.builder().id(exerciseTemplateId).workoutTemplate(workoutTemplate).build();

        when(exerciseTemplateRepository.findByIdWithWorkoutTemplateAndTrainingPlan(exerciseTemplateId)).thenReturn(Optional.of(exerciseTemplate));

        // Act & Assert
        assertThrows(ResourceOwnershipException.class, () -> {
            authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, requesterEmail);
        });
    }
    //endregion
}

