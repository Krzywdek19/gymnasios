package com.krzywdek19.user_service.dto.response;

import com.krzywdek19.user_service.model.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(
        name = "UserResponse",
        description = "User representation returned by the user service."
)
public record UserResponse(
        @Schema(
                description = "Unique user identifier.",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID id,

        @Schema(
                description = "User email address.",
                example = "john.doe@example.com"
        )
        String email,

        @Schema(
                description = "Current user account status.",
                example = "ACTIVE"
        )
        UserStatus status,

        @Schema(
                description = "Timestamp when the account was created.",
                example = "2026-03-25T10:15:30Z"
        )
        Instant createdAt,

        @Schema(
                description = "Timestamp when the account was last updated.",
                example = "2026-03-25T11:20:45Z"
        )
        Instant updatedAt
) {
}