package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.exceptions.ResourceOwnershipException;
import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.enums.TrainingPlanStatus;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;
import com.krzywdek19.workout_service.repository.TrainingPlanRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.utils.TrainingPlanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    void createPlan_shouldCreateAndReturnPlanDto() {
        // Arrange
        String userEmail = "user@example.com";
        CreateTrainingPlanRequest request = new CreateTrainingPlanRequest("My Plan");
        TrainingPlan plan = TrainingPlan.builder().name(request.name()).userEmail(userEmail).status(TrainingPlanStatus.ACTIVE).build();
        TrainingPlanDto dto = new TrainingPlanDto(UUID.randomUUID(), "My Plan", TrainingPlanStatus.ACTIVE.name(), new ArrayList<>());

        when(authentication.getName()).thenReturn(userEmail);
        when(trainingPlanRepository.save(any(TrainingPlan.class))).thenReturn(plan);
        when(trainingPlanMapper.toDto(plan)).thenReturn(dto);

        // Act
        TrainingPlanDto result = trainingPlanService.createPlan(request);

        // Assert
        assertNotNull(result);
        assertEquals(dto.name(), result.name());
        verify(trainingPlanRepository).save(any(TrainingPlan.class));
    }

    @Test
    void getPlansForCurrentUser_shouldReturnListOfDtos() {
        // Arrange
        String userEmail = "user@example.com";
        TrainingPlan plan = TrainingPlan.builder().userEmail(userEmail).build();
        TrainingPlanDto dto = new TrainingPlanDto(UUID.randomUUID(), "My Plan", TrainingPlanStatus.ACTIVE.name(), new ArrayList<>());

        when(authentication.getName()).thenReturn(userEmail);
        when(trainingPlanRepository.findAllByUserEmail(userEmail)).thenReturn(Collections.singletonList(plan));
        when(trainingPlanMapper.toDto(plan)).thenReturn(dto);

        // Act
        List<TrainingPlanDto> result = trainingPlanService.getPlansForCurrentUser();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPlanById_shouldReturnPlan_whenPlanExistsAndBelongsToUser() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";
        TrainingPlan plan = TrainingPlan.builder().id(planId).userEmail(userEmail).build();
        TrainingPlanDto planDto = new TrainingPlanDto(planId, "name", TrainingPlanStatus.ACTIVE.name(), new ArrayList<>());

        when(authentication.getName()).thenReturn(userEmail);
        when(authorizationService.verifyAndGetPlan(planId, userEmail)).thenReturn(plan);
        when(trainingPlanMapper.toDto(plan)).thenReturn(planDto);

        TrainingPlanDto result = trainingPlanService.getPlanById(planId);

        assertNotNull(result);
        verify(authorizationService).verifyAndGetPlan(planId, userEmail);
        verify(trainingPlanMapper).toDto(plan);
    }

    @Test
    void getPlanById_shouldThrowResourceNotFoundException_whenPlanDoesNotExist() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";
        when(authentication.getName()).thenReturn(userEmail);
        when(authorizationService.verifyAndGetPlan(planId, userEmail))
                .thenThrow(new ResourceNotFoundException("TrainingPlan", planId));

        assertThrows(ResourceNotFoundException.class, () -> {
            trainingPlanService.getPlanById(planId);
        });
    }

    @Test
    void getPlanById_shouldThrowResourceOwnershipException_whenPlanBelongsToAnotherUser() {
        UUID planId = UUID.randomUUID();
        String requesterEmail = "requester@example.com";
        when(authentication.getName()).thenReturn(requesterEmail);
        when(authorizationService.verifyAndGetPlan(planId, requesterEmail))
                .thenThrow(new ResourceOwnershipException("TrainingPlan", planId));

        assertThrows(ResourceOwnershipException.class, () -> {
            trainingPlanService.getPlanById(planId);
        });
    }

    @Test
    void deletePlan_shouldCallRepositoryDelete_whenPlanExistsAndBelongsToUser() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";
        TrainingPlan plan = TrainingPlan.builder().id(planId).userEmail(userEmail).build();

        when(authentication.getName()).thenReturn(userEmail);
        when(authorizationService.verifyAndGetPlan(planId, userEmail)).thenReturn(plan);

        trainingPlanService.deletePlan(planId);

        verify(authorizationService).verifyAndGetPlan(planId, userEmail);
        verify(trainingPlanRepository).delete(plan);
    }

    @Test
    void deletePlan_shouldThrowResourceOwnershipException_whenPlanBelongsToAnotherUser() {
        UUID planId = UUID.randomUUID();
        String requesterEmail = "requester@example.com";

        when(authentication.getName()).thenReturn(requesterEmail);
        doThrow(new ResourceOwnershipException("TrainingPlan", planId))
                .when(authorizationService).verifyAndGetPlan(planId, requesterEmail);

        assertThrows(ResourceOwnershipException.class, () -> {
            trainingPlanService.deletePlan(planId);
        });
    }
}
