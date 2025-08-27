package com.pms.patientservice.exception;

import com.pms.patientservice.dto.GenericResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<GenericResponseDto<String>> handleApiException(ApiException ex) {
        log.error("API Exception occurred: {}", ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(GenericResponseDto.failure(ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<GenericResponseDto<String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        log.error("Email already exists exception: {}", ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(GenericResponseDto.failure(ex.getMessage()));
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<GenericResponseDto<String>> handlePatientNotFoundException(PatientNotFoundException ex) {
        log.error("Patient not found exception: {}", ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(GenericResponseDto.failure(ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<GenericResponseDto<String>> handleConflictException(ConflictException ex) {
        log.error("Conflict exception: {}", ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(GenericResponseDto.failure(ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponseDto<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found exception: {}", ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(GenericResponseDto.failure(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponseDto<String>> handleGenericException(Exception ex) {
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(500) // Internal Server Error
                .body(GenericResponseDto.failure("An unexpected error occurred. Please try again later."));
    }
}
