package com.pms.patientservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PatientResponse {
    private UUID id;

    private String name;

    private String email;

    private String address;

    private LocalDate dateOfBirth;

}
