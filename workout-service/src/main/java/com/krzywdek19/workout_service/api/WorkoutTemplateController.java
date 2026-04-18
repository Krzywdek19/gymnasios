package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.service.WorkoutTemplateService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(
        name = "Workout Templates",
        description = "Endpoints for creating, listing, retrieving, and deleting workout templates."
)
public class WorkoutTemplateController {

    private final WorkoutTemplateService workoutTemplateService;

    @Operation(
            summary = "Create a workout template in a training plan",
            description = "Creates a workout template and attaches it to the specified training plan."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Workout template created successfully",
                    content = @Content(schema = @Schema(implementation = WorkoutTemplateDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Training plan not found or access denied")
    })
    @PostMapping("/training-plans/{planId}/workout-templates")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<WorkoutTemplateDto> createWorkoutTemplate(
            @PathVariable UUID planId,
            @Valid
            @RequestBody(
                    description = "Workout template creation payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateWorkoutTemplateRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody CreateWorkoutTemplateRequest request
    ) {
        WorkoutTemplateDto createdTemplate = workoutTemplateService.addWorkoutTemplateToPlan(planId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTemplate);
    }

    @Operation(
            summary = "Get workout templates for a training plan",
            description = "Returns all workout templates associated with the specified training plan."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Workout templates retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkoutTemplateDto.class)))
            ),
            @ApiResponse(responseCode = "404", description = "Training plan not found or access denied")
    })
    @GetMapping("/training-plans/{planId}/workout-templates")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<List<WorkoutTemplateDto>> getWorkoutTemplatesForPlan(@PathVariable UUID planId) {
        return ResponseEntity.ok(workoutTemplateService.getWorkoutTemplatesForPlan(planId));
    }

    @Operation(
            summary = "Get a workout template by id",
            description = "Returns a single workout template after ownership verification."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Workout template retrieved successfully",
                    content = @Content(schema = @Schema(implementation = WorkoutTemplateDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Workout template not found or access denied")
    })
    @GetMapping("/workout-templates/{workoutTemplateId}")
    @PreAuthorize("@access.workout(#workoutTemplateId, authentication)")
    public ResponseEntity<WorkoutTemplateDto> getWorkoutTemplateById(@PathVariable UUID workoutTemplateId) {
        return ResponseEntity.ok(workoutTemplateService.getWorkoutTemplateById(workoutTemplateId));
    }

    @PutMapping("/workout-templates/{workoutTemplateId}")
    @PreAuthorize("@access.workout(#workoutTemplateId, authentication)")
    public ResponseEntity<WorkoutTemplateDto> updateWorkoutTemplate(
            @PathVariable UUID workoutTemplateId,
            @Valid @org.springframework.web.bind.annotation.RequestBody UpdateWorkoutTemplateRequest request
    ) {
        return ResponseEntity.ok(
                workoutTemplateService.updateWorkoutTemplate(workoutTemplateId, request)
        );
    }

    @Operation(
            summary = "Delete a workout template",
            description = "Deletes a workout template after ownership verification."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workout template deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Workout template not found or access denied")
    })
    @DeleteMapping("/workout-templates/{workoutTemplateId}")
    @PreAuthorize("@access.workout(#workoutTemplateId, authentication)")
    public ResponseEntity<Void> deleteWorkoutTemplate(@PathVariable UUID workoutTemplateId) {
        workoutTemplateService.deleteWorkoutTemplate(workoutTemplateId);
        return ResponseEntity.noContent().build();
    }
}