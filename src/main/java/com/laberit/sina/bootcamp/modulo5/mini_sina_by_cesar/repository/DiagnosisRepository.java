package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Diagnosis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    Page<Diagnosis> findByPatientId(Long patientId, Pageable pageable);
}
