package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;
import com.krzywdek19.workout_service.service.TrainingPlanService;
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
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/training-plans")
@RequiredArgsConstructor
@Tag(name = "Training Plan", description = "Endpoints for managing training plans")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @Operation(summary = "Create a new training plan", description = "Creates a new training plan for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training plan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<TrainingPlanDto> createTrainingPlan(
            @RequestBody CreateTrainingPlanRequest request,
            @Parameter(description = "Email of the user performing the action", required = true) @RequestHeader("x-user-email") String userEmail) {
        TrainingPlanDto createdPlan = trainingPlanService.createPlan(userEmail, request);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all user's training plans", description = "Retrieves a list of all training plans belonging to the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of training plans")
    })
    @GetMapping
    public ResponseEntity<List<TrainingPlanDto>> getUserPlans(
            @Parameter(description = "Email of the user performing the action", required = true) @RequestHeader("x-user-email") String userEmail) {
        List<TrainingPlanDto> plans = trainingPlanService.getPlansByUserEmail(userEmail);
        return ResponseEntity.ok(plans);
    }

    @Operation(summary = "Get a training plan by ID", description = "Retrieves a specific training plan by its ID, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the training plan"),
            @ApiResponse(responseCode = "404", description = "Training plan not found or user does not have access")
    })
    @GetMapping("/{planId}")
    public ResponseEntity<TrainingPlanDto> getTrainingPlanById(
            @Parameter(description = "ID of the training plan to retrieve") @PathVariable UUID planId,
            @Parameter(description = "Email of the user performing the action", required = true) @RequestHeader("x-user-email") String userEmail) {
        TrainingPlanDto plan = trainingPlanService.getPlanById(planId, userEmail);
        return ResponseEntity.ok(plan);
    }

    @Operation(summary = "Delete a training plan", description = "Deletes a specific training plan by its ID, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Training plan deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Training plan not found or user does not have access")
    })
    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deleteTrainingPlan(
            @Parameter(description = "ID of the training plan to delete") @PathVariable UUID planId,
            @Parameter(description = "Email of the user performing the action", required = true) @RequestHeader("x-user-email") String userEmail) {
        trainingPlanService.deletePlan(planId, userEmail);
        return ResponseEntity.noContent().build();
    }
}

