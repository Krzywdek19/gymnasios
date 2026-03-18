package com.krzywdek19.workout_service.repository;

import com.krzywdek19.workout_service.model.ExerciseSession;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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
}