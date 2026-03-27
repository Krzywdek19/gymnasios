package com.krzywdek19.user_service.controller;

import com.krzywdek19.user_service.dto.response.ApiError;
import com.krzywdek19.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(
        name = "Users",
        description = "Endpoints for operations on the currently authenticated user."
)
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Delete current user",
            description = "Deletes the currently authenticated user account."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized request",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser(
            @Parameter(
                    description = "Authenticated user email injected by the gateway",
                    required = true,
                    example = "john.doe@example.com"
            )
            @RequestHeader("X-User-Email") String email,
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        userService.deleteUserByEmail(email, authorizationHeader);
        return ResponseEntity.noContent().build();
    }
}