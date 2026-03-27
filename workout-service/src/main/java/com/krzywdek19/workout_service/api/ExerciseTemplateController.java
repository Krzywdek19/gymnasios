package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.ExerciseTemplateDto;
import com.krzywdek19.workout_service.model.request.CreateExerciseTemplateRequest;
import com.krzywdek19.workout_service.model.request.UpdateExerciseTemplateRequest;
import com.krzywdek19.workout_service.service.ExerciseTemplateService;
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
        name = "Exercise Templates",
        description = "Endpoints for creating, listing, updating, and deleting exercise templates."
)
public class ExerciseTemplateController {

    private final ExerciseTemplateService exerciseTemplateService;

    @Operation(
            summary = "Add an exercise template to a workout template",
            description = "Creates an exercise template and attaches it to the specified workout template."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Exercise template created successfully",
                    content = @Content(schema = @Schema(implementation = ExerciseTemplateDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Workout template not found or access denied")
    })
    @PostMapping("/workout-templates/{templateId}/exercise-templates")
    @PreAuthorize("@access.workout(#templateId, authentication)")
    public ResponseEntity<ExerciseTemplateDto> addExerciseTemplate(
            @Parameter(
                    description = "Workout template identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID templateId,
            @Valid
            @RequestBody(
                    description = "Exercise template creation payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateExerciseTemplateRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody CreateExerciseTemplateRequest request
    ) {
        ExerciseTemplateDto createdTemplate = exerciseTemplateService.addExerciseTemplate(templateId, request);
        return new ResponseEntity<>(createdTemplate, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all exercise templates for a workout template",
            description = "Returns all exercise templates associated with the specified workout template."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Exercise templates retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExerciseTemplateDto.class)))
            ),
            @ApiResponse(responseCode = "404", description = "Workout template not found or access denied")
    })
    @GetMapping("/workout-templates/{templateId}/exercise-templates")
    @PreAuthorize("@access.workout(#templateId, authentication)")
    public ResponseEntity<List<ExerciseTemplateDto>> getExerciseTemplatesForWorkout(
            @Parameter(
                    description = "Workout template identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID templateId
    ) {
        List<ExerciseTemplateDto> templates = exerciseTemplateService.getExerciseTemplatesForWorkout(templateId);
        return ResponseEntity.ok(templates);
    }

    @Operation(
            summary = "Update an exercise template",
            description = "Updates an existing exercise template after ownership verification."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Exercise template updated successfully",
                    content = @Content(schema = @Schema(implementation = ExerciseTemplateDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Exercise template not found or access denied")
    })
    @PutMapping("/exercise-templates/{exerciseTemplateId}")
    @PreAuthorize("@access.exercise(#exerciseTemplateId, authentication)")
    public ResponseEntity<ExerciseTemplateDto> updateExerciseTemplate(
            @Parameter(
                    description = "Exercise template identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID exerciseTemplateId,
            @Valid
            @RequestBody(
                    description = "Exercise template update payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateExerciseTemplateRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody UpdateExerciseTemplateRequest request
    ) {
        ExerciseTemplateDto updatedTemplate = exerciseTemplateService.updateExerciseTemplate(exerciseTemplateId, request);
        return ResponseEntity.ok(updatedTemplate);
    }

    @Operation(
            summary = "Delete an exercise template",
            description = "Deletes an exercise template after ownership verification."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exercise template deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Exercise template not found or access denied")
    })
    @DeleteMapping("/exercise-templates/{exerciseTemplateId}")
    @PreAuthorize("@access.exercise(#exerciseTemplateId, authentication)")
    public ResponseEntity<Void> deleteExerciseTemplate(
            @Parameter(
                    description = "Exercise template identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID exerciseTemplateId
    ) {
        exerciseTemplateService.deleteExerciseTemplate(exerciseTemplateId);
        return ResponseEntity.noContent().build();
    }
}