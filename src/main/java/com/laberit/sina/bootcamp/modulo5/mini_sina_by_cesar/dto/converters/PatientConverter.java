package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

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
                patient.getGender().name()
        );
    }

    public Patient convertToEntity(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setId(patientDTO.id());
        patient.setFirstName(patientDTO.firstName());
        patient.setLastName(patientDTO.lastName());
        patient.setDateOfBirth(patientDTO.dateOfBirth());
        patient.setGender(Patient.Gender.valueOf(patientDTO.gender()));
        return patient;
    }
}
