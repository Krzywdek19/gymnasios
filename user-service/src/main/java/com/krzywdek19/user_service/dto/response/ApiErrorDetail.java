package com.krzywdek19.user_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ApiErrorDetail",
        description = "Detailed error entry describing a single invalid field or issue."
)
public record ApiErrorDetail(
        @Schema(
                description = "Name of the field related to the error.",
                example = "email"
        )
        String field,

        @Schema(
                description = "Description of the issue detected for the field.",
                example = "must be a well-formed email address"
        )
        String issue
) {
}