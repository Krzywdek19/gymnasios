package com.krzywdek19.workout_service.repository;

import com.krzywdek19.workout_service.model.WorkoutSession;
import com.krzywdek19.workout_service.model.enums.WorkoutSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, UUID> {

    Optional<WorkoutSession> findByUserEmailAndFinishedAtIsNull(String userEmail);

    Optional<WorkoutSession> findByUserEmailAndStatus(String userEmail, WorkoutSessionStatus status);

    boolean existsByUserEmailAndStatusIsNot(String userEmail, WorkoutSessionStatus status);

    List<WorkoutSession> findAllByUserEmailOrderByStartedAtDesc(String userEmail);
    List<WorkoutSession> findAllByUserEmailAndStatus(
            String userEmail,
            WorkoutSessionStatus status
    );

    List<WorkoutSession> findAllByUserEmailAndStatusAndWorkoutTemplate_Id(
            String userEmail,
            WorkoutSessionStatus status,
            UUID workoutTemplateId
    );

    Optional<WorkoutSession> findFirstByUserEmailAndStatusAndWorkoutTemplate_TrainingPlan_IdOrderByFinishedAtDesc(
            String userEmail,
            WorkoutSessionStatus status,
            UUID trainingPlanId
    );

    @Modifying
    @Query(
            value = """
                UPDATE workout_sessions
                SET workout_template_id = NULL
                WHERE workout_template_id IN (
                    SELECT id
                    FROM workout_templates
                    WHERE training_plan_id = :trainingPlanId
                )
                """,
            nativeQuery = true
    )
    void detachWorkoutTemplateReferencesByTrainingPlanId(UUID trainingPlanId);

    @Modifying
    @Query(
            value = """
                UPDATE workout_sessions
                SET workout_template_id = NULL
                WHERE workout_template_id = :workoutTemplateId
                """,
            nativeQuery = true
    )
    void detachWorkoutTemplateReferencesByWorkoutTemplateId(UUID workoutTemplateId);

}