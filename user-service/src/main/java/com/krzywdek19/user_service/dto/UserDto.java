package com.krzywdek19.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "UserDto",
        description = "Reserved DTO type for future user-related data transfer use."
)
public record UserDto() {
}