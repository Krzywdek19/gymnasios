package com.krzywdek19.user_service.service.impl;

import com.krzywdek19.user_service.exception.InvalidVerificationTokenException;
import com.krzywdek19.user_service.model.EmailVerificationToken;
import com.krzywdek19.user_service.model.User;
import com.krzywdek19.user_service.model.UserStatus;
import com.krzywdek19.user_service.repository.EmailVerificationTokenRepository;
import com.krzywdek19.user_service.repository.UserRepository;
import com.krzywdek19.user_service.service.EmailSenderService;
import com.krzywdek19.user_service.service.EmailVerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Profile("!test")
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private static final long VERIFICATION_TOKEN_TTL_SECONDS = 3600;

    private final EmailVerificationTokenRepository tokenRepository;
    private final EmailSenderService emailSenderService;
    private final UserRepository userRepository;

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    @Override
    public void createAndSendVerificationToken(User user) {
        String token = UUID.randomUUID().toString().replace("-", "");

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .user(user)
                .token(token)
                .expiresAt(Instant.now().plusSeconds(VERIFICATION_TOKEN_TTL_SECONDS))
                .build();

        tokenRepository.save(verificationToken);

        String verificationLink = buildVerificationLink(token);

        emailSenderService.sendEmail(
                user.getEmail(),
                "Verify your email",
                """
                Welcome to Gymnasios!

                Please confirm your email address by opening the link below:

                %s

                This link will expire in 1 hour.
                """.formatted(verificationLink)
        );
    }

    @Override
    @Transactional
    public void verify(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidVerificationTokenException("Invalid verification token"));

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidVerificationTokenException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);
    }

    private String buildVerificationLink(String token) {
        String normalizedBaseUrl = frontendBaseUrl.endsWith("/")
                ? frontendBaseUrl.substring(0, frontendBaseUrl.length() - 1)
                : frontendBaseUrl;

        return normalizedBaseUrl + "/verify-email.html?token=" + token;
    }
}