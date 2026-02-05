package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.WorkoutSessionDto;
import com.krzywdek19.workout_service.model.request.StartWorkoutSessionRequest;
import com.krzywdek19.workout_service.service.WorkoutSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workout-sessions")
@RequiredArgsConstructor
@Tag(name = "Workout Session", description = "Endpoints for managing workout sessions")
public class WorkoutSessionController {

    private final WorkoutSessionService workoutSessionService;

    @Operation(summary = "Start a new workout session", description = "Starts a new workout session from a workout template. A user can only have one active session at a time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Workout session started successfully"),
            @ApiResponse(responseCode = "404", description = "Workout template not found"),
            @ApiResponse(responseCode = "409", description = "User already has an active workout session")
    })
    @PostMapping
    public ResponseEntity<WorkoutSessionDto> startWorkoutSession(
            @RequestBody StartWorkoutSessionRequest request,
            @Parameter(description = "ID of the user performing the action", required = true) @RequestHeader("x-user-id") String userId) {
        WorkoutSessionDto session = workoutSessionService.startWorkoutSession(userId, request);
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the active workout session", description = "Retrieves the currently active workout session for the user, if one exists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active workout session found"),
            @ApiResponse(responseCode = "404", description = "No active workout session found for the user")
    })
    @GetMapping("/active")
    public ResponseEntity<WorkoutSessionDto> getActiveWorkoutSession(
            @Parameter(description = "ID of the user performing the action", required = true) @RequestHeader("x-user-id") String userId) {
        WorkoutSessionDto session = workoutSessionService.getActiveWorkoutSession(userId);
        return ResponseEntity.ok(session);
    }

    @Operation(summary = "Finish the active workout session", description = "Marks the currently active workout session as completed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout session finished successfully"),
            @ApiResponse(responseCode = "404", description = "No active workout session found to finish")
    })
    @PostMapping("/active/finish")
    public ResponseEntity<WorkoutSessionDto> finishWorkoutSession(
            @Parameter(description = "ID of the user performing the action", required = true) @RequestHeader("x-user-id") String userId) {
        WorkoutSessionDto session = workoutSessionService.finishWorkoutSession(userId);
        return ResponseEntity.ok(session);
    }

    @Operation(summary = "Get all workout sessions for a user", description = "Retrieves a list of all workout sessions (active and completed) for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of workout sessions")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutSessionDto>> getAllUserWorkoutSessions(
            @Parameter(description = "ID of the user performing the action", required = true) @RequestHeader("x-user-id") String userId) {
        List<WorkoutSessionDto> sessions = workoutSessionService.getAllUserWorkoutSessions(userId);
        return ResponseEntity.ok(sessions);
    }
}
