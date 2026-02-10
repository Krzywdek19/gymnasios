package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.enums.TrainingPlanStatus;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;
import com.krzywdek19.workout_service.repository.TrainingPlanRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.service.TrainingPlanService;
import com.krzywdek19.workout_service.utils.TrainingPlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingPlanMapper trainingPlanMapper;
    private final AuthorizationService authorizationService;

    @Override
    @Transactional
    public TrainingPlanDto createPlan(CreateTrainingPlanRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var trainingPlan = TrainingPlan.builder()
                .name(request.name())
                .userEmail(userEmail)
                .status(TrainingPlanStatus.ACTIVE)
                .build();
        return trainingPlanMapper.toDto(trainingPlanRepository.save(trainingPlan));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingPlanDto> getPlansForCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return trainingPlanRepository
                .findAllByUserEmail(userEmail)
                .stream().map(trainingPlanMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingPlanDto getPlanById(UUID planId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var trainingPlan = authorizationService.verifyAndGetPlan(planId, userEmail);
        return trainingPlanMapper.toDto(trainingPlan);
    }

    @Override
    @Transactional
    public void deletePlan(UUID planId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var trainingPlan = authorizationService.verifyAndGetPlan(planId, userEmail);
        trainingPlanRepository.delete(trainingPlan);
    }
}