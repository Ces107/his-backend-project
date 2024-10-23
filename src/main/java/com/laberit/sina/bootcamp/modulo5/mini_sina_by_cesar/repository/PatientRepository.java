package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUser(User user);

    @Query(value = """
        SELECT p.gender, COUNT(*)
        FROM patient p
        WHERE p.date_of_birth >= COALESCE(CAST(:startDate AS DATE), p.date_of_birth)
          AND p.date_of_birth <= COALESCE(CAST(:endDate AS DATE), p.date_of_birth)
        GROUP BY p.gender
        """, nativeQuery = true)
    List<Object[]> countPatientsByGender(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = """
        SELECT AVG(EXTRACT(YEAR FROM AGE(CURRENT_DATE, p.date_of_birth)))
        FROM patient p
        WHERE p.date_of_birth >= COALESCE(CAST(:startDate AS DATE), p.date_of_birth)
          AND p.date_of_birth <= COALESCE(CAST(:endDate AS DATE), p.date_of_birth)
        """, nativeQuery = true)
    Double getAverageAge(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = """
        SELECT COUNT(DISTINCT p.id)
        FROM patient p
        JOIN diagnosis d ON p.id = d.patient_id
        WHERE p.date_of_birth >= COALESCE(CAST(:startDate AS DATE), p.date_of_birth)
          AND p.date_of_birth <= COALESCE(CAST(:endDate AS DATE), p.date_of_birth)
        """, nativeQuery = true)
    Long countPatientsWithDiagnoses(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
