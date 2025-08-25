package com.pms.patientservice.exception;

public class EmailAlreadyExistsException extends ConflictException{
    public EmailAlreadyExistsException(String item) {
        super("Email already exists");
    }
}
