package com.krzywdek19.workout_service.service;

import com.krzywdek19.workout_service.model.dto.SetSessionDto;
import com.krzywdek19.workout_service.model.request.UpdateSetRequest;

public interface SetSessionService {
    SetSessionDto updateSetSession(Long setSessionId, String userId, UpdateSetRequest request);
    void deleteSetSession(Long setSessionId, String userId);
}
