package com.krzywdek19.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "RefreshRequest",
        description = "Request payload used to refresh an access token."
)
public record RefreshRequest(
        @Schema(
                description = "Refresh token previously issued during login.",
                example = "eyJhbGciOiJIUzI1NiJ9.refresh.token"
        )
        @NotBlank
        String refreshToken
) {
}