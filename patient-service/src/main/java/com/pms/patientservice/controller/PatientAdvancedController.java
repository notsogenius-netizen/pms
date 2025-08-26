package com.pms.patientservice.controller;

import com.pms.patientservice.dto.GenericResponseDto;
import com.pms.patientservice.dto.PatientCreation;
import com.pms.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients/advanced")
@RequiredArgsConstructor
@Slf4j
public class PatientAdvancedController {

    private final PatientService patientService;

    /**
     * Create multiple patients in bulk
     */
    @PostMapping("/bulk")
    public ResponseEntity<GenericResponseDto<List<String>>> createMultiplePatients(
            @RequestBody @Validated List<PatientCreation> patientsCreation) {
        log.info("POST /api/v1/patients/advanced/bulk - Creating {} patients in bulk", patientsCreation.size());
        List<String> createdIds = patientService.createMultiplePatients(patientsCreation);
        return ResponseEntity.ok(GenericResponseDto.success(createdIds));
    }

    /**
     * Get patients with custom sorting
     */
    @GetMapping("/sorted")
    public ResponseEntity<GenericResponseDto<List<com.pms.patientservice.dto.PatientResponse>>> getPatientsSorted(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        log.info("GET /api/v1/patients/advanced/sorted - Fetching patients sorted by {} in {} direction", sortBy, direction);
        List<com.pms.patientservice.dto.PatientResponse> patients = patientService.getAllPatientsSorted(sortBy, direction);
        return ResponseEntity.ok(GenericResponseDto.success(patients));
    }
}
