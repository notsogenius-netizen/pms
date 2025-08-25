package com.pms.patientservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public class PatientResponse {
    private UUID id;

    private String name;

    private String email;

    private String address;

    private LocalDate dateOfBirth;

    private LocalDate registeredDate;
}
