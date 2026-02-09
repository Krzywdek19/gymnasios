package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.exceptions.ResourceOwnershipException;
import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.enums.TrainingPlanStatus;
import com.krzywdek19.workout_service.repository.TrainingPlanRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.utils.TrainingPlanMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingPlanServiceImplTest {
    @Mock
    private TrainingPlanRepository trainingPlanRepository;

    @Mock
    private TrainingPlanMapper trainingPlanMapper;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private TrainingPlanServiceImpl trainingPlanService;

    @Test
    void getPlanById_shouldReturnPlan_whenPlanExistsAndBelongsToUser() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";
        TrainingPlan plan = TrainingPlan.builder().id(planId).userEmail(userEmail).build();
        TrainingPlanDto planDto = new TrainingPlanDto(planId, "name", TrainingPlanStatus.ACTIVE.name(), new ArrayList<>());

        when(authorizationService.verifyAndGetPlan(planId, userEmail)).thenReturn(plan);
        when(trainingPlanMapper.toDto(plan)).thenReturn(planDto);

        TrainingPlanDto result = trainingPlanService.getPlanById(planId, userEmail);

        assertNotNull(result);
        verify(authorizationService).verifyAndGetPlan(planId, userEmail);
        verify(trainingPlanMapper).toDto(plan);
    }

    @Test
    void getPlanById_shouldThrowResourceNotFoundException_whenPlanDoesNotExist() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";
        when(authorizationService.verifyAndGetPlan(planId, userEmail))
                .thenThrow(new ResourceNotFoundException("TrainingPlan", planId));

        assertThrows(ResourceNotFoundException.class, () -> {
            trainingPlanService.getPlanById(planId, userEmail);
        });
    }

    @Test
    void getPlanById_shouldThrowResourceOwnershipException_whenPlanBelongsToAnotherUser() {
        UUID planId = UUID.randomUUID();
        String requesterEmail = "requester@example.com";
        when(authorizationService.verifyAndGetPlan(planId, requesterEmail))
                .thenThrow(new ResourceOwnershipException("TrainingPlan", planId));

        assertThrows(ResourceOwnershipException.class, () -> {
            trainingPlanService.getPlanById(planId, requesterEmail);
        });
    }

    @Test
    void deletePlan_shouldCallRepositoryDelete_whenPlanExistsAndBelongsToUser() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";
        TrainingPlan plan = TrainingPlan.builder().id(planId).userEmail(userEmail).build();

        when(authorizationService.verifyAndGetPlan(planId, userEmail)).thenReturn(plan);

        trainingPlanService.deletePlan(planId, userEmail);

        verify(authorizationService).verifyAndGetPlan(planId, userEmail);
        verify(trainingPlanRepository).delete(plan);
    }

    @Test
    void deletePlan_shouldThrowResourceOwnershipException_whenPlanBelongsToAnotherUser() {
        UUID planId = UUID.randomUUID();
        String requesterEmail = "requester@example.com";

        doThrow(new ResourceOwnershipException("TrainingPlan", planId))
                .when(authorizationService).verifyAndGetPlan(planId, requesterEmail);

        assertThrows(ResourceOwnershipException.class, () -> {
            trainingPlanService.deletePlan(planId, requesterEmail);
        });
    }
}
