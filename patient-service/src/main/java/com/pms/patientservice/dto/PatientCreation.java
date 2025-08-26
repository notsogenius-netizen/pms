package com.pms.patientservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreation {

    @JsonProperty("patient_name")
    @NotBlank(message = "Name is required")
    @Size(min = 2, message = "Name must be more than 2 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(min = 2, message = "Email must be more than 2 characters")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @JsonProperty("date_of_birth")
    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth should be in past")
    private LocalDate dateOfBirth;

    @JsonProperty("registered_date")
    @NotNull(message = "Registered Date is required")
    private LocalDate registeredDate;

}
