package com.pms.patientservice.controller;

import com.pms.patientservice.dto.GenericResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
@Slf4j
public class HealthController {

    @GetMapping
    public ResponseEntity<GenericResponseDto<Map<String, Object>>> health() {
        log.info("GET /api/v1/health - Health check requested");
        
        Map<String, Object> healthInfo = Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "Patient Management Service",
            "version", "1.0.0"
        );
        
        return ResponseEntity.ok(GenericResponseDto.success(healthInfo));
    }

    @GetMapping("/ready")
    public ResponseEntity<GenericResponseDto<String>> readiness() {
        log.info("GET /api/v1/health/ready - Readiness check requested");
        return ResponseEntity.ok(GenericResponseDto.success("Service is ready"));
    }

    @GetMapping("/live")
    public ResponseEntity<GenericResponseDto<String>> liveness() {
        log.info("GET /api/v1/health/live - Liveness check requested");
        return ResponseEntity.ok(GenericResponseDto.success("Service is alive"));
    }
}
