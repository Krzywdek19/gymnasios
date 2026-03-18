package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.SetSessionDto;
import com.krzywdek19.workout_service.model.request.UpdateSetRequest;

import java.util.List;
import java.util.UUID;

public interface SetSessionService {

    List<SetSessionDto> getSetSessionsForExercise(UUID exerciseSessionId);

    SetSessionDto getSetSessionById(UUID setSessionId);

    SetSessionDto updateSetSession(UUID setSessionId, UpdateSetRequest request);

    void deleteSetSession(UUID setSessionId);
}