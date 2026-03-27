package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.WorkoutSessionDto;
import com.krzywdek19.workout_service.model.request.StartWorkoutSessionRequest;
import com.krzywdek19.workout_service.service.WorkoutSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workout-sessions")
@RequiredArgsConstructor
@Tag(
        name = "Workout Sessions",
        description = "Endpoints for starting, retrieving, finishing, and listing workout sessions."
)
public class WorkoutSessionController {

    private final WorkoutSessionService workoutSessionService;

    @Operation(
            summary = "Start a new workout session",
            description = "Starts a new workout session from a workout template. A user can only have one active session at a time."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Workout session started successfully",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Workout template not found"),
            @ApiResponse(responseCode = "409", description = "User already has an active workout session")
    })
    @PostMapping("/start")
    public ResponseEntity<WorkoutSessionDto> startWorkoutSession(
            @Valid
            @RequestBody(
                    description = "Workout session start payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = StartWorkoutSessionRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody StartWorkoutSessionRequest request
    ) {
        WorkoutSessionDto dto = workoutSessionService.startWorkoutSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Get workout session by id",
            description = "Retrieves a specific workout session belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Workout session found",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Workout session not found")
    })
    @GetMapping("/{workoutSessionId}")
    public ResponseEntity<WorkoutSessionDto> getWorkoutSessionById(
            @Parameter(
                    description = "Workout session identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID workoutSessionId
    ) {
        WorkoutSessionDto dto = workoutSessionService.getWorkoutSessionById(workoutSessionId);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get the active workout session",
            description = "Retrieves the currently active workout session for the authenticated user, if one exists."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Active workout session found",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "No active workout session found for the user")
    })
    @GetMapping("/active")
    public ResponseEntity<WorkoutSessionDto> getActiveWorkoutSession() {
        WorkoutSessionDto session = workoutSessionService.getActiveWorkoutSession();
        return ResponseEntity.ok(session);
    }

    @Operation(
            summary = "Finish workout session",
            description = "Marks the specified workout session as completed."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Workout session finished successfully",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Workout session not found")
    })
    @PutMapping("/{workoutSessionId}/finish")
    public ResponseEntity<WorkoutSessionDto> finishWorkoutSession(
            @Parameter(
                    description = "Workout session identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID workoutSessionId
    ) {
        WorkoutSessionDto dto = workoutSessionService.finishWorkoutSession(workoutSessionId);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get all workout sessions for the authenticated user",
            description = "Retrieves a list of all workout sessions, both active and completed, for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of workout sessions",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkoutSessionDto.class)))
            )
    })
    @GetMapping
    public ResponseEntity<List<WorkoutSessionDto>> getAllUserWorkoutSessions() {
        List<WorkoutSessionDto> sessions = workoutSessionService.getAllUserWorkoutSessions();
        return ResponseEntity.ok(sessions);
    }
}