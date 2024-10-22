// DoctorAppointmentStatsDTO.java
package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DoctorAppointmentStatsDTO {

    private final Long doctorId;
    private final String doctorName;
    private final Long canceledAppointments;



}
