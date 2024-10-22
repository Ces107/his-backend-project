// AnalyticsServiceImpl.java
package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.impl;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.AppointmentAnalyticsDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.DoctorAppointmentStatsDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.PatientAnalyticsDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.AppointmentRepository;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.PatientRepository;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.AnalyticsService;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Service implementation to provide analytics data, such as patient and appointment statistics.
 * @see com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.AnalyticsService
 */
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public PatientAnalyticsDTO getPatientAnalytics(Date startDate, Date endDate) {
        PatientAnalyticsDTO dto = new PatientAnalyticsDTO();
        dto.setTotalPatientsByGender(getTotalPatientsByGender(startDate, endDate));
        dto.setAverageAge(getAveragePatientAge(startDate, endDate));
        dto.setPatientsWithDiagnoses(getPatientsWithDiagnosesCount(startDate, endDate));

        return dto;
    }


    /**
     * Get the total number of patients by gender.
     * @param startDate the start date of the period to analyze
     * @param endDate the end date of the period to analyze
     * @return a map with the total number of patients by gender
     */
    @Schema(description = "Total number of patients categorized by gender", example = "{\"MALE\": 10, \"FEMALE\": 15, \"OTHER\": 2}")
    private Map<String, Long> getTotalPatientsByGender(Date startDate, Date endDate) {
        List<Object[]> results = patientRepository.countPatientsByGender(startDate, endDate);
        Map<String, Long> patientsByGender = new HashMap<>();
        for (Object[] result : results) {
            String gender = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            patientsByGender.put(gender, count);
        }
        return patientsByGender;
    }


    /**
     * @param startDate the start date of the period to analyze
     * @param endDate the end date of the period to analyze
     * @return the average age of patients as a double
     */
    private Double getAveragePatientAge(Date startDate, Date endDate) {
        return patientRepository.getAverageAge(startDate, endDate);
    }


    /**
     * Get the number of patients with any diagnose.
     * @param startDate the start date of the period to analyze
     * @param endDate the end date of the period to analyze
     * @return the number of patients with any diagnose
     */

    private Long getPatientsWithDiagnosesCount(Date startDate, Date endDate) {
        return patientRepository.countPatientsWithDiagnoses(startDate, endDate);
    }


    /**
     * Get appointment analytics data. This method returns a DTO with the following information:
     * - Total appointments by status
     * - Average age of patients with canceled appointments
     * - Doctors with most canceled appointments
     *
     * @param startDate the start date of the period to analyze, formatted as ISO date. Example: 2024-10-22T00:00:00Z
     * @param endDate the end date of the period to analyze, formatted as ISO date. Example: 2024-10-22T00:00:00Z
     * @return
     */
    @Override
    public AppointmentAnalyticsDTO getAppointmentAnalytics(Date startDate, Date endDate) {
        AppointmentAnalyticsDTO dto = new AppointmentAnalyticsDTO();

        // Total appointments by status
        dto.setTotalAppointmentsByStatus(getTotalAppointmentsByStatus(startDate, endDate));
        dto.setAverageAgeOfCanceledAppointments(getAverageAgeOfPatientsWithCanceledAppointments(startDate, endDate));
        dto.setDoctorsWithMostCanceledAppointments(getDoctorsWithMostCanceledAppointments(startDate, endDate));

        return dto;
    }


    /**
     * Get the total number of appointments by status.
     * @param startDate
     * @param endDate
     * @return
     */
    private Map<String, Long> getTotalAppointmentsByStatus(Date startDate, Date endDate) {
        List<Object[]> results = appointmentRepository.countAppointmentsByStatus(startDate, endDate);
        Map<String, Long> appointmentsByStatus = new HashMap<>();
        for (Object[] result : results) {
            String status = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            appointmentsByStatus.put(status, count);
        }
        return appointmentsByStatus;
    }


    /**
     * Get the average age of patients with canceled appointments.
     * @param startDate
     * @param endDate
     * @return
     */
    private Double getAverageAgeOfPatientsWithCanceledAppointments(Date startDate, Date endDate) {
        return appointmentRepository.getAverageAgeOfPatientsByStatus("CANCELLED", startDate, endDate);
    }


    /**
     * Get the doctors with the most canceled appointments.
     * @param startDate
     * @param endDate
     * @return
     */
    private List<DoctorAppointmentStatsDTO> getDoctorsWithMostCanceledAppointments(Date startDate, Date endDate) {
        List<Object[]> results = appointmentRepository.findDoctorsWithMostCanceledAppointments("CANCELLED", startDate, endDate);
        List<DoctorAppointmentStatsDTO> doctorStats = new ArrayList<>();
        for (Object[] result : results) {
            Long doctorId = ((Number) result[0]).longValue();
            String doctorName = (String) result[1];
            Long canceledAppointments = ((Number) result[2]).longValue();
            doctorStats.add(new DoctorAppointmentStatsDTO(doctorId, doctorName, canceledAppointments));
        }
        return doctorStats;
    }
}
