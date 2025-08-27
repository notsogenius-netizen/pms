package com.pms.patientservice.mapper;

import com.pms.patientservice.dto.PatientCreation;
import com.pms.patientservice.dto.PatientResponse;
import com.pms.patientservice.model.Patient;

public class PatientMapper {
    public static PatientResponse toDto(Patient patient){
        if(patient == null) return null;
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setName(patient.getName());
        response.setEmail(patient.getEmail());
        response.setAddress(patient.getAddress());
        response.setDateOfBirth(patient.getDateOfBirth());
        return response;
    }
    public static Patient toModel(PatientCreation patient) {
        if(patient == null) return null;
        Patient model = new Patient();
        model.setName(patient.getName());
        model.setEmail(patient.getEmail());
        model.setAddress(patient.getAddress());
        model.setDateOfBirth(patient.getDateOfBirth());
        model.setRegisteredDate(patient.getRegisteredDate());
        return model;
    }
}
