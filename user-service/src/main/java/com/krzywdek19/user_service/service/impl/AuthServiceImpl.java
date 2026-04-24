package com.krzywdek19.user_service.service.impl;

import com.krzywdek19.user_service.config.JwtProperties;
import com.krzywdek19.user_service.dto.request.ForgotPasswordRequest;
import com.krzywdek19.user_service.dto.request.LoginRequest;
import com.krzywdek19.user_service.dto.request.RefreshRequest;
import com.krzywdek19.user_service.dto.request.RegisterRequest;
import com.krzywdek19.user_service.dto.request.ResetPasswordRequest;
import com.krzywdek19.user_service.dto.request.VerifyRequest;
import com.krzywdek19.user_service.dto.response.TokenResponse;
import com.krzywdek19.user_service.dto.response.UserResponse;
import com.krzywdek19.user_service.exception.AccountIsNotActiveException;
import com.krzywdek19.user_service.exception.EmailTakenException;
import com.krzywdek19.user_service.exception.InvalidCredentialsException;
import com.krzywdek19.user_service.exception.InvalidRefreshTokenException;
import com.krzywdek19.user_service.exception.TooManyLoginAttemptsException;
import com.krzywdek19.user_service.mapper.UserMapper;
import com.krzywdek19.user_service.model.User;
import com.krzywdek19.user_service.model.UserStatus;
import com.krzywdek19.user_service.repository.UserRepository;
import com.krzywdek19.user_service.security.LoginRateLimiter;
import com.krzywdek19.user_service.service.AuthService;
import com.krzywdek19.user_service.service.EmailVerificationService;
import com.krzywdek19.user_service.service.JwtService;
import com.krzywdek19.user_service.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final LoginRateLimiter loginRateLimiter;
    private final JwtService jwtService;
    private final ResetPasswordService resetPasswordService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {
        String normalizedEmail = normalizeEmail(registerRequest.email());

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailTakenException("Email already in use");
        }

        User user = User.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(registerRequest.password()))
                .status(UserStatus.PENDING)
                .build();

        User createdUser = userRepository.save(user);
        emailVerificationService.createAndSendVerificationToken(createdUser);

        return UserMapper.toUserResponse(createdUser);
    }

    @Override
    public void verifyEmail(VerifyRequest request) {
        emailVerificationService.verify(request.token());
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        if (loginRateLimiter.isBlocked(normalizedEmail)) {
            throw new TooManyLoginAttemptsException("Too many login attempts. Please try again later.");
        }

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            loginRateLimiter.recordFailedAttempt(normalizedEmail);
            throw new InvalidCredentialsException("Invalid email or password");
        }

        ensureUserIsActive(user);
        loginRateLimiter.resetAttempts(normalizedEmail);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        long expiresIn = jwtProperties.accessDuration();

        return new TokenResponse("Bearer", accessToken, expiresIn, refreshToken);
    }

    @Override
    public TokenResponse refreshToken(RefreshRequest request) {
        String refreshToken = request.refreshToken();

        try {
            String username = jwtService.extractUsername(refreshToken);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

            ensureUserIsActive(user);

            if (!jwtService.isTokenValid(refreshToken, user)) {
                throw new InvalidRefreshTokenException("Invalid refresh token");
            }

            String newAccessToken = jwtService.generateAccessToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            long expiresIn = jwtProperties.accessDuration();

            return new TokenResponse("Bearer", newAccessToken, expiresIn, newRefreshToken);
        } catch (InvalidRefreshTokenException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        userRepository.findByEmail(normalizedEmail)
                .ifPresent(resetPasswordService::createAndSendResetToken);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        resetPasswordService.resetPassword(request.token(), request.newPassword());
    }

    private void ensureUserIsActive(User user) {
        if (user.getStatus() == UserStatus.ACTIVE) {
            return;
        }

        if (user.getStatus() == UserStatus.PENDING) {
            throw new AccountIsNotActiveException("Account is not active. Please verify your email address.");
        }

        if (user.getStatus() == UserStatus.DELETED) {
            throw new AccountIsNotActiveException("Account has been deleted.");
        }

        throw new AccountIsNotActiveException("Account is blocked.");
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}