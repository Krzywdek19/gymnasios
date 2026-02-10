package com.krzywdek19.workout_service.security;

import com.krzywdek19.workout_service.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("access")
@RequiredArgsConstructor
public class AccessChecks {

    private final AuthorizationService authorizationService;

    public boolean plan(UUID planId, Authentication auth) {
        authorizationService.verifyAndGetPlan(planId, auth.getName());
        return true;
    }

    public boolean workout(UUID workoutTemplateId, Authentication auth) {
        authorizationService.verifyAndGetWorkoutTemplate(workoutTemplateId, auth.getName());
        return true;
    }

    public boolean exercise(UUID exerciseTemplateId, Authentication auth) {
        authorizationService.verifyAndGetExerciseTemplate(exerciseTemplateId, auth.getName());
        return true;
    }
}