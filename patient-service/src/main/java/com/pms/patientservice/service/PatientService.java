package com.pms.patientservice.service;

import com.pms.patientservice.dto.PatientCreation;
import com.pms.patientservice.dto.PatientResponse;
import com.pms.patientservice.exception.EmailAlreadyExistsException;
import com.pms.patientservice.exception.PatientNotFoundException;
import com.pms.patientservice.mapper.PatientMapper;
import com.pms.patientservice.model.Patient;
import com.pms.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientResponse> getAllPatients() {
        log.info("Fetching all patients");
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDto).toList();
    }

    public Page<PatientResponse> getAllPatientsWithPagination(Pageable pageable) {
        log.info("Fetching patients with pagination");
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(PatientMapper::toDto);
    }

    public List<PatientResponse> getAllPatientsSorted(String sortBy, String direction) {
        log.info("Fetching patients sorted by {} in {} direction", sortBy, direction);
        Sort sort = Sort.by(Sort.Direction.fromString(direction.toUpperCase()), sortBy);
        List<Patient> patients = patientRepository.findAll(sort);
        return patients.stream().map(PatientMapper::toDto).toList();
    }

    public PatientResponse getPatientById(String id) {
        log.info("Fetching patient with id: {}", id);
        try {
            UUID patientId = UUID.fromString(id);
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new PatientNotFoundException(id));
            return PatientMapper.toDto(patient);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", id);
            throw new PatientNotFoundException(id);
        }
    }

    public String createPatient(PatientCreation body) {
        log.info("Creating new patient with name: {}", body.getName());
        
        // Check if email already exists (more efficient than findByEmail)
        if (patientRepository.existsByEmail(body.getEmail())) {
            throw new EmailAlreadyExistsException(body.getEmail());
        }
        
        Patient patient = PatientMapper.toModel(body);
        Patient savedPatient = patientRepository.save(patient);
        log.info("Patient created successfully with ID: {}", savedPatient.getId());
        return savedPatient.getId().toString();
    }

    public PatientResponse updatePatient(String id, PatientCreation updateRequest) {
        log.info("Updating patient with id: {}", id);
        
        try {
            UUID patientId = UUID.fromString(id);
            Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(id));
            
            // Check if new email conflicts with existing patients
            if (!patient.getEmail().equals(updateRequest.getEmail()) && 
                patientRepository.findByEmail(updateRequest.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException(updateRequest.getEmail());
            }
            
            // Update patient fields
            patient.setName(updateRequest.getName());
            patient.setEmail(updateRequest.getEmail());
            patient.setAddress(updateRequest.getAddress());
            patient.setDateOfBirth(updateRequest.getDateOfBirth());
            patient.setRegisteredDate(updateRequest.getRegisteredDate());
            
            Patient updatedPatient = patientRepository.save(patient);
            log.info("Patient updated successfully with ID: {}", updatedPatient.getId());
            return PatientMapper.toDto(updatedPatient);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", id);
            throw new PatientNotFoundException(id);
        }
    }

    public void deletePatient(String id) {
        log.info("Deleting patient with id: {}", id);
        
        try {
            UUID patientId = UUID.fromString(id);
            Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(id));
            
            patientRepository.delete(patient);
            log.info("Patient deleted successfully with ID: {}", id);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", id);
            throw new PatientNotFoundException(id);
        }
    }

    public List<PatientResponse> searchPatientsByName(String name) {
        log.info("Searching patients by name: {}", name);
        List<Patient> patients = patientRepository.findByNameContainingIgnoreCase(name);
        return patients.stream().map(PatientMapper::toDto).toList();
    }

    public List<PatientResponse> getPatientsByDateOfBirthRange(java.time.LocalDate startDate, 
                                                              java.time.LocalDate endDate) {
        log.info("Fetching patients with date of birth between {} and {}", startDate, endDate);
        List<Patient> patients = patientRepository.findPatientsByDateOfBirthRange(startDate, endDate);
        return patients.stream().map(PatientMapper::toDto).toList();
    }

    public boolean patientExists(String id) {
        try {
            UUID patientId = UUID.fromString(id);
            return patientRepository.existsById(patientId);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean emailExists(String email) {
        return patientRepository.existsByEmail(email);
    }

    public List<String> createMultiplePatients(List<PatientCreation> patientsCreation) {
        log.info("Creating {} patients in bulk", patientsCreation.size());
        List<String> createdIds = new java.util.ArrayList<>();
        
        for (PatientCreation patientCreation : patientsCreation) {
            String patientId = createPatient(patientCreation);
            createdIds.add(patientId);
        }
        
        log.info("Successfully created {} patients", createdIds.size());
        return createdIds;
    }

    public Object getPatientStatistics() {
        log.info("Fetching patient statistics");
        long totalPatients = patientRepository.count();
        return Map.of(
            "totalPatients", totalPatients,
            "timestamp", java.time.LocalDateTime.now()
        );
    }
}
