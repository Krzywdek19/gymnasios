package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.ExerciseTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateExerciseTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateExerciseTemplateRequest;
import com.krzywdek19.workout_service.service.ExerciseTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Exercise Template", description = "Endpoints for managing exercise templates")
public class ExerciseTemplateController {

    private final ExerciseTemplateService exerciseTemplateService;

    //CREATE
    @Operation(summary = "Add an exercise template to a workout template", description = "Creates and adds a new exercise template to a specified workout template.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exercise template created successfully"),
            @ApiResponse(responseCode = "404", description = "Workout template not found or user does not have access")
    })
    @PostMapping("/workout-templates/{templateId}/exercise-templates")
    @PreAuthorize("@access.workout(#templateId, authentication)")
    public ResponseEntity<ExerciseTemplateDto> addExerciseTemplate(
            @Parameter(description = "ID of the workout template") @PathVariable UUID templateId,
            @RequestBody CreateExerciseTemplateRequest request) {
        ExerciseTemplateDto createdTemplate = exerciseTemplateService.addExerciseTemplate(templateId, request);
        return new ResponseEntity<>(createdTemplate, HttpStatus.CREATED);
    }

    //READ
    @Operation(summary = "Get all exercise templates for a workout template", description = "Retrieves all exercise templates for a specific workout template.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of exercise templates"),
            @ApiResponse(responseCode = "404", description = "Workout template not found or user does not have access")
    })
    @GetMapping("/workout-templates/{templateId}/exercise-templates")
    @PreAuthorize("@access.workout(#templateId, authentication)")
    public ResponseEntity<List<ExerciseTemplateDto>> getExerciseTemplatesForWorkout(
            @Parameter(description = "ID of the workout template") @PathVariable UUID templateId) {
        List<ExerciseTemplateDto> templates = exerciseTemplateService.getExerciseTemplatesForWorkout(templateId);
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{exerciseTemplateId}")
    @PreAuthorize("@access.exerciseTemplate(#exerciseTemplateId, authentication)")
    public ResponseEntity<ExerciseTemplateDto> getExerciseTemplateById(
            @PathVariable UUID exerciseTemplateId
    ) {
        ExerciseTemplateDto dto = exerciseTemplateService.getExerciseTemplateById(exerciseTemplateId);
        return ResponseEntity.ok(dto);
    }

    //UPDATE
    @Operation(summary = "Update an exercise template", description = "Updates an existing exercise template, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise template updated successfully"),
            @ApiResponse(responseCode = "404", description = "Exercise template not found or user does not have access")
    })
    @PutMapping("/exercise-templates/{exerciseTemplateId}")
    @PreAuthorize("@access.exercise(#exerciseTemplateId, authentication)")
    public ResponseEntity<ExerciseTemplateDto> updateExerciseTemplate(
            @Parameter(description = "ID of the exercise template to update") @PathVariable UUID exerciseTemplateId,
            @RequestBody UpdateExerciseTemplateRequest request) {
        ExerciseTemplateDto updatedTemplate = exerciseTemplateService.updateExerciseTemplate(exerciseTemplateId, request);
        return ResponseEntity.ok(updatedTemplate);
    }

    //DELETE
    @Operation(summary = "Delete an exercise template", description = "Deletes a specific exercise template by its ID, verifying user ownership.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exercise template deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Exercise template not found or user does not have access")
    })
    @DeleteMapping("/exercise-templates/{exerciseTemplateId}")
    @PreAuthorize("@access.exercise(#exerciseTemplateId, authentication)")
    public ResponseEntity<Void> deleteExerciseTemplate(
            @Parameter(description = "ID of the exercise template to delete") @PathVariable UUID exerciseTemplateId) {
        exerciseTemplateService.deleteExerciseTemplate(exerciseTemplateId);
        return ResponseEntity.noContent().build();
    }
}
