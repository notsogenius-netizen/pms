package com.pms.patientservice.exception;

public class PatientNotFoundException extends ResourceNotFoundException{
    public PatientNotFoundException(String nameOrEmail) {
        super("Patient not found: " + nameOrEmail);
    }
}
