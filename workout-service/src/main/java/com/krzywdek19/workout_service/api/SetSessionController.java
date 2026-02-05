package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.SetSessionDto;
import com.krzywdek19.workout_service.model.request.UpdateSetRequest;
import com.krzywdek19.workout_service.service.SetSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/set-sessions")
@RequiredArgsConstructor
@Tag(name = "Set Session", description = "Endpoints for managing individual sets within an exercise session")
public class SetSessionController {

    private final SetSessionService setSessionService;

    @Operation(summary = "Update a set session", description = "Updates the details of a specific set, such as reps and weight, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set session updated successfully"),
            @ApiResponse(responseCode = "404", description = "Set session not found or user does not have access")
    })
    @PutMapping("/{setSessionId}")
    public ResponseEntity<SetSessionDto> updateSetSession(
            @Parameter(description = "ID of the set session to update") @PathVariable Long setSessionId,
            @RequestBody UpdateSetRequest request,
            @Parameter(description = "ID of the user performing the action", required = true) @RequestHeader("x-user-id") String userId) {
        SetSessionDto updatedSet = setSessionService.updateSetSession(setSessionId, userId, request);
        return ResponseEntity.ok(updatedSet);
    }

    @Operation(summary = "Delete a set session", description = "Deletes a specific set session by its ID, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Set session deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Set session not found or user does not have access")
    })
    @DeleteMapping("/{setSessionId}")
    public ResponseEntity<Void> deleteSetSession(
            @Parameter(description = "ID of the set session to delete") @PathVariable Long setSessionId,
            @Parameter(description = "ID of the user performing the action", required = true) @RequestHeader("x-user-id") String userId) {
        setSessionService.deleteSetSession(setSessionId, userId);
        return ResponseEntity.noContent().build();
    }
}
