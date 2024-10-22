package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientService {
    Page<Patient> getAllPatients(Pageable pageable);
    Optional<Patient> getPatientById(Long id);
    Patient savePatient(Patient patient);
    void deletePatient(Long id);
}