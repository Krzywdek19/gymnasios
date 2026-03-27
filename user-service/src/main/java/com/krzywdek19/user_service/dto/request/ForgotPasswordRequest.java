package com.krzywdek19.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "ForgotPasswordRequest",
        description = "Request payload used to start the password reset flow."
)
public record ForgotPasswordRequest(
        @Schema(
                description = "Email address of the account that should receive the password reset message.",
                example = "john.doe@example.com"
        )
        @Email
        @NotBlank
        String email
) {
}