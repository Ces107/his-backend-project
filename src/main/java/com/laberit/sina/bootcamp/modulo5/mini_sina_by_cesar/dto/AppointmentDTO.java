package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

import java.util.Date;

public record AppointmentDTO(Long id, Date date, String status, Long patientId, Long doctorId) {
}
