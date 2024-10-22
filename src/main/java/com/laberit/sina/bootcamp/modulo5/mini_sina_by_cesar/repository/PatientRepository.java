package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = """
            SELECT p.gender, COUNT(*)
            FROM patient p
            WHERE (:startDate IS NULL OR p.date_of_birth >= :startDate)
              AND (:endDate IS NULL OR p.date_of_birth <= :endDate)
            GROUP BY p.gender
            """, nativeQuery = true)
    List<Object[]> countPatientsByGender(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = """
            SELECT AVG(TIMESTAMPDIFF(YEAR, p.date_of_birth, CURDATE()))
            FROM patient p
            WHERE (:startDate IS NULL OR p.date_of_birth >= :startDate)
              AND (:endDate IS NULL OR p.date_of_birth <= :endDate)
            """, nativeQuery = true)
    Double getAverageAge(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = """
            SELECT COUNT(DISTINCT p.id)
            FROM patient p
            JOIN diagnosis d ON p.id = d.patient_id
            WHERE (:startDate IS NULL OR p.date_of_birth >= :startDate)
              AND (:endDate IS NULL OR p.date_of_birth <= :endDate)
            """, nativeQuery = true)
    Long countPatientsWithDiagnoses(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
