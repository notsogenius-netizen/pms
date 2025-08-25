package com.pms.patientservice.exception;

import com.pms.patientservice.dto.GenericResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<GenericResponseDto<String>> handleApiException(ApiException ex) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(GenericResponseDto.failure(ex.getMessage()));
    }
}
