package com.krzywdek19.workout_service.repository;

import com.krzywdek19.workout_service.model.ExerciseSession;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseSessionRepository extends JpaRepository<ExerciseSession, UUID> {

    @Query("""
        select es
        from ExerciseSession es
        join fetch es.workoutSession ws
        where es.id = :id
    """)
    Optional<ExerciseSession> findByIdWithWorkoutSession(@Param("id") UUID id);

    @EntityGraph(attributePaths = {"sets"})
    List<ExerciseSession> findAllByWorkoutSessionIdOrderByOrderIndexAsc(UUID workoutSessionId);

    @Modifying
    @Query(
            value = """
                    UPDATE exercise_sessions
                    SET exercise_template_id = NULL
                    WHERE exercise_template_id IN (
                        SELECT et.id
                        FROM exercise_templates et
                        JOIN workout_templates wt ON et.workout_template_id = wt.id
                        WHERE wt.training_plan_id = :trainingPlanId
                    )
                    """,
            nativeQuery = true
    )
    void detachExerciseTemplateReferencesByTrainingPlanId(UUID trainingPlanId);

    @Modifying
    @Query(
            value = """
                    UPDATE exercise_sessions
                    SET exercise_template_id = NULL
                    WHERE exercise_template_id IN (
                        SELECT id
                        FROM exercise_templates
                        WHERE workout_template_id = :workoutTemplateId
                    )
                    """,
            nativeQuery = true
    )
    void detachExerciseTemplateReferencesByWorkoutTemplateId(UUID workoutTemplateId);

    @Modifying
    @Query(
            value = """
                    UPDATE exercise_sessions
                    SET exercise_template_id = NULL
                    WHERE exercise_template_id = :exerciseTemplateId
                    """,
            nativeQuery = true
    )
    void detachExerciseTemplateReferencesByExerciseTemplateId(UUID exerciseTemplateId);

}