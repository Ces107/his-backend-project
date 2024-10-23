package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Diagnosis;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.Disease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    Optional<Diagnosis> findByPatientAndDisease(Patient patient, Disease disease);

    Page<Diagnosis> findByPatientId(Long patientId, Pageable pageable);
}
