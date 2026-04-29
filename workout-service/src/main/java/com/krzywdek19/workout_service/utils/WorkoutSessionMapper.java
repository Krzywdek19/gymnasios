package com.krzywdek19.workout_service.utils;

import com.krzywdek19.workout_service.model.WorkoutSession;
import com.krzywdek19.workout_service.model.dto.WorkoutSessionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {ExerciseSessionMapper.class})
public interface WorkoutSessionMapper {

    @Mapping(target = "workoutTemplateId", source = "workoutSession", qualifiedByName = "workoutTemplateId")
    @Mapping(target = "workoutTemplateName", source = "workoutSession", qualifiedByName = "workoutTemplateName")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "exercises", source = "exercises")
    WorkoutSessionDto toDto(WorkoutSession workoutSession);

    @Named("workoutTemplateId")
    default UUID workoutTemplateId(WorkoutSession workoutSession) {
        if (workoutSession.getWorkoutTemplate() == null) {
            return null;
        }

        return workoutSession.getWorkoutTemplate().getId();
    }

    @Named("workoutTemplateName")
    default String workoutTemplateName(WorkoutSession workoutSession) {
        if (workoutSession.getWorkoutTemplate() == null) {
            return null;
        }

        return workoutSession.getWorkoutTemplate().getName();
    }
}