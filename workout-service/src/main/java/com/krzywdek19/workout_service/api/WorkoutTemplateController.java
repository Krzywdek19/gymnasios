package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.service.WorkoutTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Workout Template", description = "Endpoints for managing workout templates")
public class WorkoutTemplateController {

    private final WorkoutTemplateService workoutTemplateService;

    //CREATE
    @Operation(summary = "Add a workout template to a training plan", description = "Creates and adds a new workout template to a specified training plan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Workout template created successfully"),
            @ApiResponse(responseCode = "404", description = "Training plan not found or user does not have access")
    })
    @PostMapping("/training-plans/{planId}/workout-templates")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<WorkoutTemplateDto> addWorkoutTemplateToPlan(
            @Parameter(description = "ID of the training plan") @PathVariable UUID planId,
            @RequestBody CreateWorkoutTemplateRequest request) {
        WorkoutTemplateDto createdTemplate = workoutTemplateService.addWorkoutTemplateToPlan(planId, request);
        return new ResponseEntity<>(createdTemplate, HttpStatus.CREATED);
    }

    //READ
    @Operation(summary = "Get all workout templates for a training plan", description = "Retrieves all workout templates associated with a specific training plan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of workout templates"),
            @ApiResponse(responseCode = "404", description = "Training plan not found or user does not have access")
    })
    @GetMapping("/training-plans/{planId}/workout-templates")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<List<WorkoutTemplateDto>> getWorkoutTemplatesForPlan(
            @Parameter(description = "ID of the training plan") @PathVariable UUID planId) {
        List<WorkoutTemplateDto> templates = workoutTemplateService.getWorkoutTemplatesForPlan(planId);
        return ResponseEntity.ok(templates);
    }

    @Operation(summary = "Get a workout template by ID", description = "Retrieves a specific workout template by its ID, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the workout template"),
            @ApiResponse(responseCode = "404", description = "Workout template not found or user does not have access")
    })
    @GetMapping("/workout-templates/{templateId}")
    @PreAuthorize("@access.workout(#templateId, authentication)")
    public ResponseEntity<WorkoutTemplateDto> getWorkoutTemplateById(
            @Parameter(description = "ID of the workout template to retrieve") @PathVariable UUID templateId) {
        WorkoutTemplateDto template = workoutTemplateService.getWorkoutTemplateById(templateId);
        return ResponseEntity.ok(template);
    }

    //UPDATE
    @PutMapping("/{templateId}")
    @PreAuthorize("@access.workout(#templateId, authentication)")
    public ResponseEntity<WorkoutTemplateDto> updateWorkoutTemplate(
            @PathVariable UUID templateId,
            @Valid @RequestBody UpdateWorkoutTemplateRequest request
    ) {
        WorkoutTemplateDto updated = workoutTemplateService.updateWorkoutTemplate(templateId, request);
        return ResponseEntity.ok(updated);
    }

    //DELETE
    @Operation(summary = "Delete a workout template", description = "Deletes a specific workout template by its ID, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Workout template deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Workout template not found or user does not have access")
    })
    @DeleteMapping("/workout-templates/{templateId}")
    @PreAuthorize("@access.workout(#templateId, authentication)")
    public ResponseEntity<Void> deleteWorkoutTemplate(
            @Parameter(description = "ID of the workout template to delete") @PathVariable UUID templateId) {
        workoutTemplateService.deleteWorkoutTemplate(templateId);
        return ResponseEntity.noContent().build();
    }
}
