package com.krzywdek19.user_service.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "UserStatus",
        description = "Lifecycle status of a user account."
)
public enum UserStatus {

    @Schema(description = "The account exists but is waiting for email verification.")
    PENDING,

    @Schema(description = "The account is verified and can be used normally.")
    ACTIVE,

    @Schema(description = "The account is blocked and cannot be used.")
    LOCKED,

    @Schema(description = "The account was deleted.")
    DELETED
}