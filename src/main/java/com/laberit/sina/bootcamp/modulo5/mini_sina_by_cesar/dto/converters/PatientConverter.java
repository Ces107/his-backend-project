package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.converters;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.CreatePatientDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.PatientDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientConverter {

    public PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender()
        );
    }

    public Patient convertToEntity(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setId(patientDTO.id());
        patient.setFirstName(patientDTO.firstName());
        patient.setLastName(patientDTO.lastName());
        patient.setDateOfBirth(patientDTO.dateOfBirth());
        patient.setGender(patientDTO.gender());
        return patient;
    }

    public Patient convertToEntityNoId(CreatePatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setDateOfBirth(patientDTO.dateOfBirth());
        patient.setGender(patientDTO.gender());
        patient.setId(patientDTO.userId());
        return patient;
    }
}
