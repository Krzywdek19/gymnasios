package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user in security context");
        }

        String userEmail = authentication.getName();

        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalStateException("Authenticated user email is missing");
        }

        return userEmail;
    }
}