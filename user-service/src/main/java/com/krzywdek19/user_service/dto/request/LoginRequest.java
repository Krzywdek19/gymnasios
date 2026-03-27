package com.krzywdek19.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "LoginRequest",
        description = "Request payload used to authenticate a user."
)
public record LoginRequest(
        @Schema(
                description = "User email address.",
                example = "john.doe@example.com"
        )
        @Email
        @NotBlank
        String email,

        @Schema(
                description = "User password.",
                example = "StrongPassword123!",
                accessMode = Schema.AccessMode.WRITE_ONLY
        )
        @NotBlank
        String password
) {
}