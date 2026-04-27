package com.krzywdek19.workout_service.repository;

import com.krzywdek19.workout_service.model.TrainingPlan;
import com.krzywdek19.workout_service.model.enums.TrainingPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, UUID> {
    List<TrainingPlan> findAllByUserEmail(String userEmail);

    boolean existsByUserEmailAndStatus(String userEmail, TrainingPlanStatus status);

    List<TrainingPlan> findAllByUserEmailAndStatus(String userEmail, TrainingPlanStatus status);

    Optional<TrainingPlan> findFirstByUserEmailAndStatusOrderByUpdatedAtDesc(
            String userEmail,
            TrainingPlanStatus status
    );
}