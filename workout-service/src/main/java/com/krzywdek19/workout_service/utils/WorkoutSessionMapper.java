package com.krzywdek19.workout_service.utils;

import com.krzywdek19.workout_service.model.WorkoutSession;
import com.krzywdek19.workout_service.model.dto.WorkoutSessionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ExerciseSessionMapper.class})
public interface WorkoutSessionMapper {

    @Mapping(target = "workoutTemplateId", source = "workoutTemplate.id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "exercises", source = "exercises")
    WorkoutSessionDto toDto(WorkoutSession workoutSession);
}