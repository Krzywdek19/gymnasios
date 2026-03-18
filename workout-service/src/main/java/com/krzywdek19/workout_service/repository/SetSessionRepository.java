package com.krzywdek19.workout_service.repository;

import com.krzywdek19.workout_service.model.SetSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SetSessionRepository extends JpaRepository<SetSession, UUID> {

    @Query("""
        select ss
        from SetSession ss
        join fetch ss.exerciseSession es
        join fetch es.workoutSession ws
        where ss.id = :id
    """)
    Optional<SetSession> findByIdWithExerciseSessionAndWorkoutSession(@Param("id") UUID id);

    List<SetSession> findAllByExerciseSessionIdOrderByOrderIndexAsc(UUID exerciseSessionId);
}