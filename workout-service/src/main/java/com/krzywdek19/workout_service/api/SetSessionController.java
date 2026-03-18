package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.SetSessionDto;
import com.krzywdek19.workout_service.model.request.UpdateSetRequest;
import com.krzywdek19.workout_service.service.SetSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Set Session", description = "Endpoints for managing individual sets within an exercise session")
public class SetSessionController {

    private final SetSessionService setSessionService;

    @Operation(
            summary = "Get all set sessions for an exercise session",
            description = "Retrieves all sets belonging to a specific exercise session owned by the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set sessions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Exercise session not found or user does not have access")
    })
    @GetMapping("/exercise-sessions/{exerciseSessionId}/set-sessions")
    public ResponseEntity<List<SetSessionDto>> getSetSessionsForExercise(
            @PathVariable UUID exerciseSessionId
    ) {
        List<SetSessionDto> sets = setSessionService.getSetSessionsForExercise(exerciseSessionId);
        return ResponseEntity.ok(sets);
    }

    @Operation(
            summary = "Get set session by ID",
            description = "Retrieves a single set session owned by the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set session retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Set session not found or user does not have access")
    })
    @GetMapping("/set-sessions/{setSessionId}")
    public ResponseEntity<SetSessionDto> getSetSessionById(
            @PathVariable UUID setSessionId
    ) {
        SetSessionDto setSession = setSessionService.getSetSessionById(setSessionId);
        return ResponseEntity.ok(setSession);
    }

    @Operation(
            summary = "Update a set session",
            description = "Updates the details of a specific set, such as reps, weight, rir and completed status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set session updated successfully"),
            @ApiResponse(responseCode = "404", description = "Set session not found or user does not have access")
    })
    @PutMapping("/set-sessions/{setSessionId}")
    public ResponseEntity<SetSessionDto> updateSetSession(
            @PathVariable UUID setSessionId,
            @Valid @RequestBody UpdateSetRequest request
    ) {
        SetSessionDto updatedSet = setSessionService.updateSetSession(setSessionId, request);
        return ResponseEntity.ok(updatedSet);
    }

    @Operation(
            summary = "Delete a set session",
            description = "Deletes a specific set session by its ID, verifying ownership."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Set session deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Set session not found or user does not have access")
    })
    @DeleteMapping("/set-sessions/{setSessionId}")
    public ResponseEntity<Void> deleteSetSession(
            @PathVariable UUID setSessionId
    ) {
        setSessionService.deleteSetSession(setSessionId);
        return ResponseEntity.noContent().build();
    }
}