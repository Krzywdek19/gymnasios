package com.krzywdek19.workout_service.repository;

import com.krzywdek19.workout_service.model.ExerciseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ExerciseTemplateRepository extends JpaRepository<ExerciseTemplate, UUID> {
    @Query("SELECT et FROM ExerciseTemplate et WHERE et.workoutTemplate.id = :workoutTemplateId")
    List<ExerciseTemplate> findAllByWorkoutTemplateId(UUID workoutTemplateId);
    @Query("SELECT et FROM ExerciseTemplate et JOIN FETCH et.workoutTemplate wt JOIN FETCH wt.trainingPlan WHERE et.id = :id")
    Optional<ExerciseTemplate> findByIdWithWorkoutTemplateAndTrainingPlan(@Param("id") UUID id);
}

