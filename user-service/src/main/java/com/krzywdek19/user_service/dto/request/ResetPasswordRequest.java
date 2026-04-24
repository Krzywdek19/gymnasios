package com.krzywdek19.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
        name = "ResetPasswordRequest",
        description = "Request payload used to reset a password using a reset token."
)
public record ResetPasswordRequest(
        @Schema(
                description = "Password reset token sent to the user.",
                example = "9df1d1d8-12ab-43a8-a68d-0e9a7ebf3d63"
        )
        @NotBlank
        String token,

        @Schema(
                description = "New password. It must contain at least 8 characters, one uppercase letter, one digit and one special character.",
                example = "NewStrongPassword123!",
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
        String newPassword
) {
}