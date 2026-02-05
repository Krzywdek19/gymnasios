package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.ExerciseSessionDto;
import com.krzywdek19.workout_service.service.ExerciseSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Exercise Session", description = "Endpoints for managing exercise sessions within a workout")
public class ExerciseSessionController {

    private final ExerciseSessionService exerciseSessionService;

    @Operation(summary = "Get all exercise sessions for a workout session", description = "Retrieves a list of all exercise sessions for a specific workout session, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of exercise sessions"),
            @ApiResponse(responseCode = "404", description = "Workout session not found or user does not have access")
    })
    @GetMapping("/workout-sessions/{sessionId}/exercise-sessions")
    public ResponseEntity<List<ExerciseSessionDto>> getExerciseSessionsForWorkout(
            @Parameter(description = "ID of the workout session") @PathVariable Long sessionId,
            @Parameter(description = "ID of the user performing the action", required = true) @RequestHeader("x-user-id") String userId) {
        List<ExerciseSessionDto> sessions = exerciseSessionService.getExerciseSessionsForWorkout(sessionId, userId);
        return ResponseEntity.ok(sessions);
    }

    @Operation(summary = "Get an exercise session by ID", description = "Retrieves a specific exercise session by its ID, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the exercise session"),
            @ApiResponse(responseCode = "404", description = "Exercise session not found or user does not have access")
    })
    @GetMapping("/exercise-sessions/{exerciseSessionId}")
    public ResponseEntity<ExerciseSessionDto> getExerciseSessionById(
            @Parameter(description = "ID of the exercise session to retrieve") @PathVariable Long exerciseSessionId,
            @Parameter(description = "ID of the user performing the action", required = true) @RequestHeader("x-user-id") String userId) {
        ExerciseSessionDto session = exerciseSessionService.getExerciseSessionById(exerciseSessionId, userId);
        return ResponseEntity.ok(session);
    }
}
