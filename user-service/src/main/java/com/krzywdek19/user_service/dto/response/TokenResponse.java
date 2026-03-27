package com.krzywdek19.user_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "TokenResponse",
        description = "Authentication response containing issued access and refresh tokens."
)
public record TokenResponse(
        @Schema(
                description = "Token type used for authorization.",
                example = "Bearer"
        )
        String tokenType,

        @Schema(
                description = "JWT access token.",
                example = "eyJhbGciOiJIUzI1NiJ9.access.token"
        )
        String accessToken,

        @Schema(
                description = "Access token lifetime expressed in seconds.",
                example = "360000"
        )
        long expiresIn,

        @Schema(
                description = "JWT refresh token.",
                example = "eyJhbGciOiJIUzI1NiJ9.refresh.token"
        )
        String refreshToken
) {
}