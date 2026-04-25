package com.krzywdek19.workout_service.response;

import java.util.List;

public record ApiError(
        String code,
        String message,
        List<ApiErrorDetail> details
) {

}
