package com.krzywdek19.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
        @Size(max = 320)
        String email,

        @Schema(
                description = "User password.",
                example = "StrongPassword123!",
                minLength = 1,
                maxLength = 128,
                accessMode = Schema.AccessMode.WRITE_ONLY
        )
        @NotBlank
        @Size(max = 128)
        String password
) {
}