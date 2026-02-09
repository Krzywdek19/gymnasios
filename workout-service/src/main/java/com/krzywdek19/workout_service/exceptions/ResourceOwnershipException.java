package com.krzywdek19.workout_service.exceptions;

import java.util.UUID;

public class ResourceOwnershipException extends RuntimeException {
    public ResourceOwnershipException(String resourceName, UUID resourceId) {
        super(resourceName + " with id: " + resourceId + " is not owned by user.");
    }
}
