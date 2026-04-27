package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.WorkoutSessionDto;
import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.StartWorkoutSessionRequest;
import com.krzywdek19.workout_service.service.WorkoutSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
            summary = "Start workout session from selected template",
            description = """
                    Starts a new workout session from the selected workout template.
                    The backend generates exercise sessions and set sessions based on the workout template structure.
                    A user can only have one active workout session at a time.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Workout session started successfully",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Workout template not found"),
            @ApiResponse(responseCode = "409", description = "User already has an active workout session")
    })
    @PostMapping
    public ResponseEntity<WorkoutSessionDto> createWorkoutSession(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Workout session start payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = StartWorkoutSessionRequest.class))
            )
            @RequestBody StartWorkoutSessionRequest request
    ) {
        WorkoutSessionDto dto = workoutSessionService.startWorkoutSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Start next workout session",
            description = """
                    Starts a new workout session from the next workout template in the currently active training plan.
                    The next workout is calculated based on the last finished workout session from the active plan.
                    If there is no finished session yet, the first workout template from the active plan is used.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Next workout session started successfully",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Active training plan or workout template not found"),
            @ApiResponse(responseCode = "409", description = "User already has an active workout session")
    })
    @PostMapping("/start-next")
    public ResponseEntity<WorkoutSessionDto> startNextWorkoutSession() {
        WorkoutSessionDto dto = workoutSessionService.startNextWorkoutSession();
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Get active workout session",
            description = "Retrieves the currently active workout session for the authenticated user, if one exists."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Active workout session found",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "No active workout session found for the user")
    })
    @GetMapping("/active")
    public ResponseEntity<WorkoutSessionDto> getActiveWorkoutSession() {
        return ResponseEntity.ok(workoutSessionService.getActiveWorkoutSession());
    }

    @Operation(
            summary = "Get next workout template",
            description = """
                    Returns the next workout template from the currently active training plan.
                    This endpoint does not create a workout session. It only tells the mobile app what workout will be started next.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Next workout template retrieved successfully",
                    content = @Content(schema = @Schema(implementation = WorkoutTemplateDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Active training plan or workout template not found")
    })
    @GetMapping("/next-workout")
    public ResponseEntity<WorkoutTemplateDto> getNextWorkoutTemplate() {
        return ResponseEntity.ok(workoutSessionService.getNextWorkoutTemplate());
    }

    @Operation(
            summary = "Get all workout sessions",
            description = "Retrieves all workout sessions, both active and completed, for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved workout sessions",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkoutSessionDto.class)))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutSessionDto>> getWorkoutSessions() {
        return ResponseEntity.ok(workoutSessionService.getAllUserWorkoutSessions());
    }

    @Operation(
            summary = "Get workout session by id",
            description = "Retrieves a specific workout session belonging to the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Workout session found",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Workout session not found")
    })
    @GetMapping("/{workoutSessionId}")
    public ResponseEntity<WorkoutSessionDto> getWorkoutSessionById(@PathVariable UUID workoutSessionId) {
        return ResponseEntity.ok(workoutSessionService.getWorkoutSessionById(workoutSessionId));
    }

    @Operation(
            summary = "Finish workout session",
            description = "Marks the specified workout session as finished."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Workout session finished successfully",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Workout session not found")
    })
    @PostMapping("/{workoutSessionId}/finish")
    public ResponseEntity<WorkoutSessionDto> finishWorkoutSession(@PathVariable UUID workoutSessionId) {
        return ResponseEntity.ok(workoutSessionService.finishWorkoutSession(workoutSessionId));
    }
}