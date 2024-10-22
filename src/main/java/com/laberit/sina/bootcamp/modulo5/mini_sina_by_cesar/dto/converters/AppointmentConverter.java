package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.converters;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.AppointmentDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Appointment;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.AppointmentStatus;
import org.springframework.stereotype.Component;

@Component
public class AppointmentConverter {

    public AppointmentDTO convertToDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDate(),
                appointment.getStatus().name(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getId()
        );
    }

    public Appointment convertToEntity(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentDTO.date());
        appointment.setStatus(AppointmentStatus.valueOf(appointmentDTO.status()));
        return appointment;
    }
}
