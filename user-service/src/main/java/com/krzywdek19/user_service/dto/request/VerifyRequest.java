package com.krzywdek19.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "VerifyRequest",
        description = "Request payload used to verify a newly created account."
)
public record VerifyRequest(
        @Schema(
                description = "Email verification token sent to the user.",
                example = "f8a0a595-4d2f-4e74-a985-4c8c7c59b4f6"
        )
        @NotBlank
        String token
) {
}