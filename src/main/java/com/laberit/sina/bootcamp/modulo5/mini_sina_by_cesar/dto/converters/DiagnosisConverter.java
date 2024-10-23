package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.converters;


import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.DiagnosisDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Diagnosis;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.Disease;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.DiagnosisStatus;
import org.springframework.stereotype.Component;

@Component
public class DiagnosisConverter {

    public DiagnosisDTO convertToDTO(Diagnosis diagnosis) {
        return new DiagnosisDTO(
                diagnosis.getId(),
                diagnosis.getDisease().name(),
                diagnosis.getStatus().name(),
                diagnosis.getPatient().getId()
        );
    }

    public Diagnosis convertToEntity(DiagnosisDTO diagnosisDTO) {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDisease(Disease.valueOf(diagnosisDTO.getDisease()));
        diagnosis.setStatus(DiagnosisStatus.valueOf(diagnosisDTO.getStatus()));
        return diagnosis;
    }
}