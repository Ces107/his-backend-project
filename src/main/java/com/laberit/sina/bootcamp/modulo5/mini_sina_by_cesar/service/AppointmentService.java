package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service to manage appointments.
 */
public interface AppointmentService {
    Page<Appointment> getAllAppointments(Pageable pageable);
    Optional<Appointment> getAppointmentById(Long id);
    Appointment saveAppointment(Appointment appointment);
    void deleteAppointment(Long id);
}