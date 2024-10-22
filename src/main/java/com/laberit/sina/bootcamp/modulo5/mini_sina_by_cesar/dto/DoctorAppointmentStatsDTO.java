// DoctorAppointmentStatsDTO.java
package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

public class DoctorAppointmentStatsDTO {

    private Long doctorId;
    private String doctorName;
    private Long canceledAppointments;

    public DoctorAppointmentStatsDTO(Long doctorId, String doctorName, Long canceledAppointments) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.canceledAppointments = canceledAppointments;
    }

    // Getters and setters
    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getCanceledAppointments() {
        return canceledAppointments;
    }
}
