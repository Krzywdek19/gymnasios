package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.exceptions.ResourceNotFoundException;
import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.enums.TrainingPlanStatus;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;
import com.krzywdek19.workout_service.model.request.UpdateTrainingPlanRequest;
import com.krzywdek19.workout_service.repository.TrainingPlanRepository;
import com.krzywdek19.workout_service.service.AuthorizationService;
import com.krzywdek19.workout_service.service.CurrentUserService;
import com.krzywdek19.workout_service.service.TrainingPlanService;
import com.krzywdek19.workout_service.utils.TrainingPlanMapper;
import lombok.RequiredArgsConstructor;
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
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public TrainingPlanDto createPlan(CreateTrainingPlanRequest request) {
        String userEmail = currentUserService.getCurrentUserEmail();

        boolean userAlreadyHasActivePlan = trainingPlanRepository
                .existsByUserEmailAndStatus(userEmail, TrainingPlanStatus.ACTIVE);

        var trainingPlan = TrainingPlan.builder()
                .name(request.name())
                .userEmail(userEmail)
                .status(userAlreadyHasActivePlan ? TrainingPlanStatus.INACTIVE : TrainingPlanStatus.ACTIVE)
                .build();

        return trainingPlanMapper.toDto(trainingPlanRepository.save(trainingPlan));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingPlanDto> getPlansForCurrentUser() {
        String userEmail = currentUserService.getCurrentUserEmail();

        return trainingPlanRepository
                .findAllByUserEmail(userEmail)
                .stream()
                .map(trainingPlanMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingPlanDto getPlanById(UUID planId) {
        String userEmail = currentUserService.getCurrentUserEmail();

        TrainingPlan trainingPlan = authorizationService.verifyAndGetPlan(planId, userEmail);

        return trainingPlanMapper.toDto(trainingPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingPlanDto getActivePlan() {
        String userEmail = currentUserService.getCurrentUserEmail();

        TrainingPlan activePlan = trainingPlanRepository
                .findFirstByUserEmailAndStatusOrderByUpdatedAtDesc(userEmail, TrainingPlanStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active training plan not found."));

        return trainingPlanMapper.toDto(activePlan);
    }

    @Override
    @Transactional
    public TrainingPlanDto activatePlan(UUID planId) {
        String userEmail = currentUserService.getCurrentUserEmail();

        TrainingPlan planToActivate = authorizationService.verifyAndGetPlan(planId, userEmail);

        trainingPlanRepository
                .findAllByUserEmailAndStatus(userEmail, TrainingPlanStatus.ACTIVE)
                .forEach(activePlan -> activePlan.setStatus(TrainingPlanStatus.INACTIVE));

        planToActivate.setStatus(TrainingPlanStatus.ACTIVE);

        return trainingPlanMapper.toDto(trainingPlanRepository.save(planToActivate));
    }

    @Override
    @Transactional
    public TrainingPlanDto updatePlan(UUID planId, UpdateTrainingPlanRequest request) {
        String userEmail = currentUserService.getCurrentUserEmail();

        TrainingPlan trainingPlan = authorizationService.verifyAndGetPlan(planId, userEmail);

        trainingPlan.setName(request.name());

        return trainingPlanMapper.toDto(trainingPlanRepository.save(trainingPlan));
    }

    @Override
    @Transactional
    public void deletePlan(UUID planId) {
        String userEmail = currentUserService.getCurrentUserEmail();

        TrainingPlan trainingPlan = authorizationService.verifyAndGetPlan(planId, userEmail);

        trainingPlanRepository.delete(trainingPlan);
    }
}