package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.impl;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Diagnosis;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.DiagnosisRepository;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.DiagnosisService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to manage diagnoses.
 * @see com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.DiagnosisService
 */
@Service
@Transactional
public class DiagnosisServiceImpl implements DiagnosisService {
    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Override
    public Page<Diagnosis> getAllDiagnoses(Pageable pageable) {
        return diagnosisRepository.findAll(pageable);
    }

    @Override
    public Optional<Diagnosis> getDiagnosisById(Long id) {
        return diagnosisRepository.findById(id);
    }

    @Override
    public Diagnosis saveDiagnosis(Diagnosis diagnosis) {
        return diagnosisRepository.save(diagnosis);
    }

    @Override
    public void deleteDiagnosis(Long id) {
        if (diagnosisRepository.existsById(id)) {
            diagnosisRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Diagnosis with id " + id + " does not exist.");
        }
    }

    @Override
    public Page<Diagnosis> getDiagnosesByPatient(Long patientId, Pageable pageable){
        return diagnosisRepository.findByPatientId(patientId, pageable);
    }


}

