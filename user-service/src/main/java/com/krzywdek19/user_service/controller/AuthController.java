package com.krzywdek19.user_service.controller;

import com.krzywdek19.user_service.dto.request.ForgotPasswordRequest;
import com.krzywdek19.user_service.dto.request.LoginRequest;
import com.krzywdek19.user_service.dto.request.RefreshRequest;
import com.krzywdek19.user_service.dto.request.RegisterRequest;
import com.krzywdek19.user_service.dto.request.ResetPasswordRequest;
import com.krzywdek19.user_service.dto.request.VerifyRequest;
import com.krzywdek19.user_service.dto.response.ApiError;
import com.krzywdek19.user_service.dto.response.TokenResponse;
import com.krzywdek19.user_service.dto.response.UserResponse;
import com.krzywdek19.user_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Endpoints for registration, email verification, login, token refresh, and password recovery."
)
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns the created user resource."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid registration payload",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email address is already taken",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid
            @RequestBody(
                    description = "Registration request payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody RegisterRequest request
    ) {
        var userResponse = authService.register(request);
        var uri = URI.create("/users/" + userResponse.id());
        return ResponseEntity.created(uri).body(userResponse);
    }

    @Operation(
            summary = "Verify email",
            description = "Verifies a user account using an email verification token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Email verified successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid verification token or invalid request payload",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Verification token not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping("/verify")
    public ResponseEntity<Void> verifyEmail(
            @Valid
            @RequestBody(
                    description = "Email verification payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = VerifyRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody VerifyRequest request
    ) {
        authService.verifyEmail(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Log in",
            description = "Authenticates the user and returns access and refresh tokens."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid login payload",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials or inactive account",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many login attempts",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid
            @RequestBody(
                    description = "Login payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody LoginRequest request
    ) {
        var token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Refresh token",
            description = "Issues a new access token using a valid refresh token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid refresh token payload",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid refresh token",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @Valid
            @RequestBody(
                    description = "Refresh token payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RefreshRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody RefreshRequest request
    ) {
        var token = authService.refreshToken(request);
        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Request password reset",
            description = "Starts the password reset flow for the given email address."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password reset request accepted"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid forgot password payload",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping("/password/forgot")
    public ResponseEntity<Void> forgotPassword(
            @Valid
            @RequestBody(
                    description = "Forgot password payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ForgotPasswordRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ForgotPasswordRequest request
    ) {
        authService.forgotPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Reset password",
            description = "Resets the user password using a valid reset token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password reset successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid reset password payload or invalid token",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reset token not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(
            @Valid
            @RequestBody(
                    description = "Reset password payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ResetPasswordRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ResetPasswordRequest request
    ) {
        authService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }
}