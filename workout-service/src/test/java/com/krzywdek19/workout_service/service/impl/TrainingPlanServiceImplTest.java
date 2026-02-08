package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.repository.TrainingPlanRepository;
import com.krzywdek19.workout_service.utils.TrainingPlanMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingPlanServiceImplTest {
    @Mock
    private TrainingPlanRepository trainingPlanRepository;

    @Mock
    private TrainingPlanMapper trainingPlanMapper;

    @InjectMocks
    private TrainingPlanServiceImpl trainingPlanService;

    @Test
    void getPlanById_shouldThrowResourceNotFoundException_whenPlanDoesNotExist() {
        UUID planId = UUID.randomUUID();
        String userEmail = "user@example.com";
        when(trainingPlanRepository.findById(planId)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> {
            trainingPlanService.getPlanById(planId, userEmail);
        });
    }

    @Test
    void getPlanById_shouldThrowResourceNotFoundException_whenPlanBelongsToAnotherUser() {
        UUID planId = UUID.randomUUID();
        String ownerEmail = "owner@example.com";
        String requesterEmail = "requester@example.com";
        TrainingPlan plan = TrainingPlan.builder().id(planId).userEmail(ownerEmail).build();
        when(trainingPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        assertThrows(ResourceNotFoundException.class, () -> {
            trainingPlanService.getPlanById(planId, requesterEmail);
        });
    }
}
