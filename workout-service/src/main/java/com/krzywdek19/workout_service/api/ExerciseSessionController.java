package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.ExerciseSessionDto;
import com.krzywdek19.workout_service.service.ExerciseSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(
        name = "Exercise Sessions",
        description = "Endpoints for retrieving exercise sessions within a workout session."
)
public class ExerciseSessionController {

    private final ExerciseSessionService exerciseSessionService;

    @Operation(
            summary = "Get all exercise sessions for a workout session",
            description = "Retrieves a list of all exercise sessions for a specific workout session belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of exercise sessions",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExerciseSessionDto.class)))
            ),
            @ApiResponse(responseCode = "404", description = "Workout session not found or user does not have access")
    })
    @GetMapping("/workout-sessions/{workoutSessionId}/exercise-sessions")
    public ResponseEntity<List<ExerciseSessionDto>> getExerciseSessionsForWorkout(
            @Parameter(
                    description = "Workout session identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID workoutSessionId
    ) {
        List<ExerciseSessionDto> sessions =
                exerciseSessionService.getExerciseSessionsForWorkout(workoutSessionId);
        return ResponseEntity.ok(sessions);
    }

    @Operation(
            summary = "Get an exercise session by id",
            description = "Retrieves a specific exercise session by its identifier, verifying ownership."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the exercise session",
                    content = @Content(schema = @Schema(implementation = ExerciseSessionDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Exercise session not found or user does not have access")
    })
    @GetMapping("/exercise-sessions/{exerciseSessionId}")
    public ResponseEntity<ExerciseSessionDto> getExerciseSessionById(
            @Parameter(
                    description = "Exercise session identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID exerciseSessionId
    ) {
        ExerciseSessionDto session = exerciseSessionService.getExerciseSessionById(exerciseSessionId);
        return ResponseEntity.ok(session);
    }
}