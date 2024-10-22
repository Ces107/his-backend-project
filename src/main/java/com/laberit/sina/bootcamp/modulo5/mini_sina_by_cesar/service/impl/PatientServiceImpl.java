package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.impl;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.PatientRepository;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.PatientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to manage patients.
 * @see com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.PatientService
 */
@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Page<Patient> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Patient with id " + id + " does not exist.");
        }
    }
}


