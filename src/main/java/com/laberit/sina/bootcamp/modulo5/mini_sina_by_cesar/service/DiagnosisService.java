package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Diagnosis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DiagnosisService {
    Page<Diagnosis> getAllDiagnoses(Pageable pageable);
    Optional<Diagnosis> getDiagnosisById(Long id);
    Diagnosis saveDiagnosis(Diagnosis diagnosis);
    void deleteDiagnosis(Long id);

    Page<Diagnosis> getDiagnosesByPatient(Long patientId, Pageable pageable);
}