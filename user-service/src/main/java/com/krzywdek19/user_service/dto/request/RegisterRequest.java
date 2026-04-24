package com.krzywdek19.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
                description = "Password for the new account. It must contain at least 8 characters, one uppercase letter, one digit and one special character.",
                example = "StrongPassword123!",
                minLength = 8,
                maxLength = 128,
                accessMode = Schema.AccessMode.WRITE_ONLY
        )
        @Size(min = 8, max = 128)
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,128}$",
                message = "password must contain at least 8 characters, one uppercase letter, one digit and one special character"
        )
        @NotBlank
        String password
) {
}