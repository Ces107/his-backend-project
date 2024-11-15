package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Appointment;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    List<Appointment> findByStatus(AppointmentStatus status);

    Optional<Appointment> findByPatientAndDoctorAndDate(Patient patient, User doctor, Date date);

    @Query(value = """
            SELECT a.status, COUNT(*)
            FROM appointment a
            WHERE (:startDate IS NULL OR a.date >= :startDate)
              AND (:endDate IS NULL OR a.date <= :endDate)
            GROUP BY a.status
            """, nativeQuery = true)
    List<Object[]> countAppointmentsByStatus(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = """
            SELECT AVG(EXTRACT(YEAR FROM AGE(CURRENT_DATE, p.date_of_birth)))
            FROM appointment a
            JOIN patient p ON a.patient_id = p.id
            WHERE a.status = :status
              AND (:startDate IS NULL OR a.date >= :startDate)
              AND (:endDate IS NULL OR a.date <= :endDate)
            """, nativeQuery = true)
    Double getAverageAgeOfPatientsByStatus(@Param("status") String status,
                                           @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate);

    @Query(value = """
            SELECT d.id AS doctorId, CONCAT(d.first_name, ' ', d.last_name) AS doctorName, COUNT(*) AS canceledAppointments
            FROM appointment a
            JOIN users d ON a.doctor_id = d.id
            WHERE a.status = :status
              AND (:startDate IS NULL OR a.date >= :startDate)
              AND (:endDate IS NULL OR a.date <= :endDate)
            GROUP BY d.id, d.first_name, d.last_name
            ORDER BY canceledAppointments DESC
            """, nativeQuery = true)
    List<Object[]> findDoctorsWithMostStatusAppointments(@Param("status") String status,
                                                         @Param("startDate") Date startDate,
                                                         @Param("endDate") Date endDate);
}
