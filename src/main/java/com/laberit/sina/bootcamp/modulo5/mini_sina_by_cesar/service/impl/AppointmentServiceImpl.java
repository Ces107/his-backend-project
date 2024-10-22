package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.impl;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Appointment;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.AppointmentRepository;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.AppointmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to manage appointments.
 * @see com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.AppointmentService
 */

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public Page<Appointment> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }


    /**
     * Delete an appointment by id.
     * @param id
     */
    @Override
    public void deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Appointment with id " + id + " does not exist.");
        }
    }
}
