package com.pms.patientservice.exception;

public class EmailAlreadyExistsException extends ConflictException{
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
