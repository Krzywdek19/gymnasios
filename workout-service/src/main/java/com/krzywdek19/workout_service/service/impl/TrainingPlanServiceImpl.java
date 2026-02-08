package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.enums.TrainingPlanStatus;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;
import com.krzywdek19.workout_service.repository.TrainingPlanRepository;
import com.krzywdek19.workout_service.service.TrainingPlanService;
import com.krzywdek19.workout_service.utils.TrainingPlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingPlanMapper trainingPlanMapper;
    @Override
    @Transactional
    public TrainingPlanDto createPlan(String userEmail, CreateTrainingPlanRequest request) {
        Instant now = Instant.now();
        var trainingPlan = TrainingPlan.builder()
                .name(request.name())
                .userEmail(userEmail)
                .status(TrainingPlanStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return trainingPlanMapper.toDto(trainingPlanRepository.save(trainingPlan));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingPlanDto> getPlansByUserEmail(String userEmail) {
        return trainingPlanRepository
                .findAllByUserEmail(userEmail)
                .stream().map(trainingPlanMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingPlanDto getPlanById(UUID planId, String userEmail) {
        return trainingPlanMapper.toDto(getPlanByIdThatBelongsToUser(planId, userEmail));
    }

    @Override
    @Transactional
    public void deletePlan(UUID planId, String userEmail) {
        var trainingPlan = getPlanByIdThatBelongsToUser(planId, userEmail);
        trainingPlanRepository.delete(trainingPlan);
    }

    private TrainingPlan getPlanByIdThatBelongsToUser(UUID planId, String userEmail) {
        return trainingPlanRepository.findById(planId)
                .filter(plan -> plan.getUserEmail().equals(userEmail))
                .orElseThrow(() -> new ResourceNotFoundException(TrainingPlan.class.getName(), planId));
    }
}
