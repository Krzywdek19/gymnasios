package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.WorkoutTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateWorkoutTemplateRequest;
import com.krzywdek19.workout_service.service.WorkoutTemplateService;
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
            summary = "Add a workout template to a training plan",
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
    public ResponseEntity<WorkoutTemplateDto> addWorkoutTemplateToPlan(
            @Parameter(
                    description = "Training plan identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
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
        return new ResponseEntity<>(createdTemplate, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all workout templates for a training plan",
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
    public ResponseEntity<List<WorkoutTemplateDto>> getWorkoutTemplatesForPlan(
            @Parameter(
                    description = "Training plan identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID planId
    ) {
        List<WorkoutTemplateDto> templates = workoutTemplateService.getWorkoutTemplatesForPlan(planId);
        return ResponseEntity.ok(templates);
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
    @GetMapping("/workout-templates/{templateId}")
    @PreAuthorize("@access.workout(#templateId, authentication)")
    public ResponseEntity<WorkoutTemplateDto> getWorkoutTemplateById(
            @Parameter(
                    description = "Workout template identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID templateId
    ) {
        WorkoutTemplateDto template = workoutTemplateService.getWorkoutTemplateById(templateId);
        return ResponseEntity.ok(template);
    }

    @Operation(
            summary = "Delete a workout template",
            description = "Deletes a workout template after ownership verification."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workout template deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Workout template not found or access denied")
    })
    @DeleteMapping("/workout-templates/{templateId}")
    @PreAuthorize("@access.workout(#templateId, authentication)")
    public ResponseEntity<Void> deleteWorkoutTemplate(
            @Parameter(
                    description = "Workout template identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID templateId
    ) {
        workoutTemplateService.deleteWorkoutTemplate(templateId);
        return ResponseEntity.noContent().build();
    }
}