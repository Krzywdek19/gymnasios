package com.krzywdek19.user_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "ApiError",
        description = "Standard error response returned by the user service."
)
public record ApiError(
        @Schema(
                description = "Application-specific error code.",
                example = "VALIDATION_ERROR"
        )
        String code,

        @Schema(
                description = "Human-readable error message.",
                example = "Request validation failed."
        )
        String message,

        @Schema(
                description = "Optional list of field-level or detailed error entries."
        )
        List<ApiErrorDetail> details
) {
}