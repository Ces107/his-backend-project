// AppointmentAnalyticsDTO.java
package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class AppointmentAnalyticsDTO {

    // Getters and setters
    private Map<String, Long> totalAppointmentsByStatus;
    private Double averageAgeOfCanceledAppointments;
    private List<DoctorAppointmentStatsDTO> doctorsWithMostCanceledAppointments;

}
