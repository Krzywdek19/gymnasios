package com.krzywdek19.workout_service.service.impl;

import com.krzywdek19.workout_service.service.CurrentUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {
    private static final String USER_EMAIL_HEADER = "X-User-Email";

    private final HttpServletRequest request;

    public String getCurrentUserEmail() {
        String userEmail = request.getHeader(USER_EMAIL_HEADER);

        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalStateException("Missing X-User-Email header");
        }

        return userEmail;
    }
}