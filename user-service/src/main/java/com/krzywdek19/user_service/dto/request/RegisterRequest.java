package com.krzywdek19.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        name = "RegisterRequest",
        description = "Request payload used to register a new user account."
)
public record RegisterRequest(
        @Schema(
                description = "Unique email address for the new account.",
                example = "john.doe@example.com"
        )
        @Email
        @NotBlank
        String email,

        @Schema(
                description = "Password for the new account. It must contain between 8 and 128 characters.",
                example = "StrongPassword123!",
                minLength = 8,
                maxLength = 128,
                accessMode = Schema.AccessMode.WRITE_ONLY
        )
        @Size(min = 8, max = 128)
        @NotBlank
        String password
) {
}