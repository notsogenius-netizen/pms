package com.pms.patientservice.controller;

import com.pms.patientservice.dto.GenericResponseDto;
import com.pms.patientservice.dto.PatientCreation;
import com.pms.patientservice.dto.PatientResponse;
import com.pms.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patient Management", description = "APIs for managing patients")
public class PatientController {

    private final PatientService patientService;

    /**
     * Get all patients
     */
    @GetMapping
    @Operation(summary = "Get All Patients")
    public ResponseEntity<GenericResponseDto<List<PatientResponse>>> getAllPatients() {
        log.info("GET /api/v1/patients - Fetching all patients");
        List<PatientResponse> patients = patientService.getAllPatients();
        return ResponseEntity.ok(GenericResponseDto.success(patients));
    }

    /**
     * Get all patients with pagination
     */
    @GetMapping("/paginated")
    @Operation(summary = "Get Patients with Pagination")
    public ResponseEntity<GenericResponseDto<Page<PatientResponse>>> getAllPatientsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        log.info("GET /api/v1/patients/paginated - Fetching patients with pagination (page={}, size={})", page, size);
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PatientResponse> patients = patientService.getAllPatientsWithPagination(pageable);
        
        return ResponseEntity.ok(GenericResponseDto.success(patients));
    }

    /**
     * Get patient by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a Patient by ID")
    public ResponseEntity<GenericResponseDto<PatientResponse>> getPatientById(@PathVariable String id) {
        log.info("GET /api/v1/patients/{} - Fetching patient by ID", id);
        PatientResponse patient = patientService.getPatientById(id);
        return ResponseEntity.ok(GenericResponseDto.success(patient));
    }

    /**
     * Create a new patient
     */
    @PostMapping
    @Operation(summary = "Create a new Patient")
    public ResponseEntity<GenericResponseDto<String>> createPatient(@RequestBody @Validated PatientCreation patientCreation) {
        log.info("POST /api/v1/patients - Creating new patient with name: {}", patientCreation.getName());
        String patientId = patientService.createPatient(patientCreation);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponseDto.success(patientId));
    }

    /**
     * Update an existing patient
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a new Patient")
    public ResponseEntity<GenericResponseDto<PatientResponse>> updatePatient(
            @PathVariable String id,
            @RequestBody @Validated PatientCreation patientUpdate) {
        log.info("PUT /api/v1/patients/{} - Updating patient", id);
        PatientResponse updatedPatient = patientService.updatePatient(id, patientUpdate);
        return ResponseEntity.ok(GenericResponseDto.success(updatedPatient));
    }

    /**
     * Delete a patient
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Patient")
    public ResponseEntity<GenericResponseDto<String>> deletePatient(@PathVariable String id) {
        log.info("DELETE /api/v1/patients/{} - Deleting patient", id);
        patientService.deletePatient(id);
        return ResponseEntity.ok(GenericResponseDto.success("Patient deleted successfully"));
    }

    /**
     * Search patients by name
     */
    @GetMapping("/search")
    @Operation(summary = "Search a Patient by name")
    public ResponseEntity<GenericResponseDto<List<PatientResponse>>> searchPatientsByName(
            @RequestParam String name) {
        log.info("GET /api/v1/patients/search - Searching patients by name: {}", name);
        List<PatientResponse> patients = patientService.searchPatientsByName(name);
        return ResponseEntity.ok(GenericResponseDto.success(patients));
    }

    /**
     * Get patients by date of birth range
     */
    @GetMapping("/by-dob")
    @Operation(summary = "Get Patients by Date of Birth Range")
    public ResponseEntity<GenericResponseDto<List<PatientResponse>>> getPatientsByDateOfBirthRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        log.info("GET /api/v1/patients/by-dob - Fetching patients with DOB between {} and {}", startDate, endDate);
        List<PatientResponse> patients = patientService.getPatientsByDateOfBirthRange(startDate, endDate);
        return ResponseEntity.ok(GenericResponseDto.success(patients));
    }

    /**
     * Check if patient exists
     */
    @GetMapping("/{id}/exists")
    @Operation(summary = "Check if Patient exists by ID")
    public ResponseEntity<GenericResponseDto<Boolean>> checkPatientExists(@PathVariable String id) {
        log.info("GET /api/v1/patients/{}/exists - Checking if patient exists", id);
        boolean exists = patientService.patientExists(id);
        return ResponseEntity.ok(GenericResponseDto.success(exists));
    }

    /**
     * Check if email exists
     */
    @GetMapping("/check-email")
    @Operation(summary = "Check if email exists")
    public ResponseEntity<GenericResponseDto<Boolean>> checkEmailExists(@RequestParam String email) {
        log.info("GET /api/v1/patients/check-email - Checking if email exists: {}", email);
        boolean exists = patientService.emailExists(email);
        return ResponseEntity.ok(GenericResponseDto.success(exists));
    }

    /**
     * Get patient statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get Patient Statistics")
    public ResponseEntity<GenericResponseDto<Object>> getPatientStatistics() {
        log.info("GET /api/v1/patients/statistics - Fetching patient statistics");
        Object statistics = patientService.getPatientStatistics();
        return ResponseEntity.ok(GenericResponseDto.success(statistics));
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<GenericResponseDto<String>> health() {
        return ResponseEntity.ok(GenericResponseDto.success("Patient Service is running"));
    }
}
