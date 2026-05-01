package com.krzywdek19.workout_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exercise_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ExerciseTemplate {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_template_id", nullable = false)
    private WorkoutTemplate workoutTemplate;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int setsCount;
    @Column(nullable = false)
    private String reps;
    @Builder.Default
    @Column(nullable = false)
    private Integer restBetweenSetsSeconds = 120;
    @Builder.Default
    @Column(nullable = false)
    private Integer restAfterExerciseSeconds = 180;
    @Column(nullable = false)
    private int orderIndex;
    private String notes;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;
    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;
}
