package com.krzywdek19.workout_service.api;

import com.krzywdek19.workout_service.model.dto.TrainingPlanDto;
import com.krzywdek19.workout_service.model.request.CreateTrainingPlanRequest;
import com.krzywdek19.workout_service.service.TrainingPlanService;
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
@RequestMapping("/api/v1/training-plans")
@RequiredArgsConstructor
@Tag(
        name = "Training Plans",
        description = "Endpoints for creating, listing, retrieving, and deleting training plans."
)
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @Operation(
            summary = "Create a training plan",
            description = "Creates a new training plan for the current user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Training plan created successfully",
                    content = @Content(schema = @Schema(implementation = TrainingPlanDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping
    public ResponseEntity<TrainingPlanDto> createTrainingPlan(
            @Valid
            @RequestBody(
                    description = "Training plan creation payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateTrainingPlanRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody CreateTrainingPlanRequest request
    ) {
        TrainingPlanDto createdPlan = trainingPlanService.createPlan(request);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
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
            )
    })
    @GetMapping
    public ResponseEntity<List<TrainingPlanDto>> getUserPlans() {
        List<TrainingPlanDto> plans = trainingPlanService.getPlansForCurrentUser();
        return ResponseEntity.ok(plans);
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
            @ApiResponse(responseCode = "404", description = "Training plan not found or access denied")
    })
    @GetMapping("/{planId}")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<TrainingPlanDto> getTrainingPlanById(
            @Parameter(
                    description = "Training plan identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID planId
    ) {
        TrainingPlanDto plan = trainingPlanService.getPlanById(planId);
        return ResponseEntity.ok(plan);
    }

    @Operation(
            summary = "Delete a training plan",
            description = "Deletes a training plan after ownership verification."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Training plan deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Training plan not found or access denied")
    })
    @DeleteMapping("/{planId}")
    @PreAuthorize("@access.plan(#planId, authentication)")
    public ResponseEntity<Void> deleteTrainingPlan(
            @Parameter(
                    description = "Training plan identifier",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID planId
    ) {
        trainingPlanService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }
}