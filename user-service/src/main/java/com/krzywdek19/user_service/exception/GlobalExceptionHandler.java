package com.krzywdek19.user_service.exception;

import com.krzywdek19.user_service.dto.response.ApiError;
import com.krzywdek19.user_service.dto.response.ApiErrorDetail;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<ApiError> handleEmailTaken(EmailTakenException ex) {
        return build(
                HttpStatus.CONFLICT,
                "EMAIL_ALREADY_TAKEN",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(AccountIsNotActiveException.class)
    public ResponseEntity<ApiError> handleAccountNotActive(AccountIsNotActiveException ex) {
        return build(
                HttpStatus.FORBIDDEN,
                "ACCOUNT_NOT_ACTIVE",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex) {
        return build(
                HttpStatus.UNAUTHORIZED,
                "INVALID_CREDENTIALS",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler({
            InvalidRefreshTokenException.class,
            UnauthorizedException.class
    })
    public ResponseEntity<ApiError> handleUnauthorized(RuntimeException ex) {
        return build(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler({
            InvalidVerificationTokenException.class,
            InvalidResetTokenException.class,
            ResetTokenExpiredException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        return build(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        return build(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(TooManyLoginAttemptsException.class)
    public ResponseEntity<ApiError> handleTooManyLoginAttempts(TooManyLoginAttemptsException ex) {
        return build(
                HttpStatus.TOO_MANY_REQUESTS,
                "TOO_MANY_LOGIN_ATTEMPTS",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiErrorDetail> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toDetail)
                .toList();

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Request validation failed.");

        return build(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                message,
                details
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        return build(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "Unexpected server error.",
                null
        );
    }

    private ApiErrorDetail toDetail(FieldError fieldError) {
        return new ApiErrorDetail(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String code,
            String message,
            List<ApiErrorDetail> details
    ) {
        return ResponseEntity
                .status(status)
                .body(new ApiError(code, message, details));
    }
}