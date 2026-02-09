package com.krzywdek19.workout_service.repository;

import com.krzywdek19.workout_service.model.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, UUID> {
    // N+1 problem workaround - fetch workout templates for a training plan and user in a single query
    @Query("SELECT wt FROM WorkoutTemplate wt JOIN wt.trainingPlan tp WHERE tp.id = :planId AND tp.userEmail = :userEmail")
    List<WorkoutTemplate> findAllByTrainingPlanIdAndUserEmail(@Param("planId") UUID planId, @Param("userEmail") String userEmail);

    @Query("SELECT wt FROM WorkoutTemplate wt JOIN FETCH wt.trainingPlan WHERE wt.id = :templateId")
    Optional<WorkoutTemplate> findByIdWithTrainingPlan(@Param("templateId") UUID templateId);
}

