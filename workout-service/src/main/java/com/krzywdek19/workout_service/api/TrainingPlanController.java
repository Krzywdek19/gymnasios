package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;
import com.krzywdek19.workout_service.model.request.UpdateTrainingPlanRequest;
import com.krzywdek19.workout_service.service.TrainingPlanService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/training-plans")
@RequiredArgsConstructor
@Tag(
        name = "Training Plans",
        description = "Endpoints for creating, listing, retrieving, activating, updating, and deleting training plans."
)
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @Operation(
            summary = "Create a training plan",
            description = """
                    Creates a new training plan for the current user.
                    If the user does not have any active training plan yet, the created plan becomes active automatically.
                    Otherwise, it is created as inactive.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Training plan created successfully",
                    content = @Content(schema = @Schema(implementation = TrainingPlanDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @PostMapping
    public ResponseEntity<TrainingPlanDto> createTrainingPlan(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Training plan creation payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateTrainingPlanRequest.class))
            )
            @RequestBody CreateTrainingPlanRequest request
    ) {
        TrainingPlanDto createdPlan = trainingPlanService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }

    @Operation(
            summary = "Get active training plan",
            description = "Returns the currently active training plan for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Active training plan retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainingPlanDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Active training plan not found")
    })
    @GetMapping("/active")
    public ResponseEntity<TrainingPlanDto> getActiveTrainingPlan() {
        return ResponseEntity.ok(trainingPlanService.getActivePlan());
    }

    @Operation(
            summary = "Get current user's training plans",
            description = "Returns all training plans that belong to the current user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Training plans retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainingPlanDto.class)))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @GetMapping
    public ResponseEntity<List<TrainingPlanDto>> getTrainingPlans() {
        return ResponseEntity.ok(trainingPlanService.getPlansForCurrentUser());
    }

    @Operation(
            summary = "Get a training plan by id",
            description = "Returns a single training plan after ownership verification."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Training plan retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainingPlanDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "User has no access to this training plan"),
            @ApiResponse(responseCode = "404", description = "Training plan not found")
    })
    @GetMapping("/{planId}")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<TrainingPlanDto> getTrainingPlanById(@PathVariable UUID planId) {
        return ResponseEntity.ok(trainingPlanService.getPlanById(planId));
    }

    @Operation(
            summary = "Activate a training plan",
            description = """
                    Marks the selected training plan as active for the authenticated user.
                    All previously active plans belonging to this user are changed to inactive.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Training plan activated successfully",
                    content = @Content(schema = @Schema(implementation = TrainingPlanDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "User has no access to this training plan"),
            @ApiResponse(responseCode = "404", description = "Training plan not found")
    })
    @PutMapping("/{planId}/activate")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<TrainingPlanDto> activateTrainingPlan(@PathVariable UUID planId) {
        return ResponseEntity.ok(trainingPlanService.activatePlan(planId));
    }

    @Operation(
            summary = "Update a training plan",
            description = "Updates a training plan after ownership verification."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Training plan updated successfully",
                    content = @Content(schema = @Schema(implementation = TrainingPlanDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "User has no access to this training plan"),
            @ApiResponse(responseCode = "404", description = "Training plan not found")
    })
    @PutMapping("/{planId}")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<TrainingPlanDto> updateTrainingPlan(
            @PathVariable UUID planId,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Training plan update payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateTrainingPlanRequest.class))
            )
            @RequestBody UpdateTrainingPlanRequest request
    ) {
        return ResponseEntity.ok(trainingPlanService.updatePlan(planId, request));
    }

    @Operation(
            summary = "Delete a training plan",
            description = "Deletes a training plan after ownership verification."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Training plan deleted successfully"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "User has no access to this training plan"),
            @ApiResponse(responseCode = "404", description = "Training plan not found")
    })
    @DeleteMapping("/{planId}")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<Void> deleteTrainingPlan(@PathVariable UUID planId) {
        trainingPlanService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }
}