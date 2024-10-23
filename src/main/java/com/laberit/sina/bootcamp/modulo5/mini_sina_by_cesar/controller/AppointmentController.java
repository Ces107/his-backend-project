package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.controller;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.AppointmentDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.converters.AppointmentConverter;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.exception.ResourceNotFoundException;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Appointment;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.AppointmentStatus;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.AppointmentService;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.PatientService;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/mini-sina/v1/patients/{patientId}/appointments")
@Tag(
        name = "Appointment",
        description = "Controller for managing appointments related to a specific patient. Access restricted to doctors and administrators."
)
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentConverter appointmentConverter;
    @Autowired
    private AnalyticsController analyticsController;

    /**
     * Creates a new appointment for a specific patient.
     * Access restricted to doctors and administrators.
     *
     * @param patientId ID of the patient.
     * @param appointmentDTO Appointment details in the request body.
     * @return The created appointment.
     */
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('PATIENT')")
    @Operation(
            summary = "Create new appointment",
            description = "Creates a new appointment for a specific patient. Requires DOCTOR or ADMIN roles."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment created successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class),
                            examples = @ExampleObject(value = "{ 'id': 1, 'doctorId': 2, 'status': 'PENDING', 'date': '2024-10-22' }")
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient or Doctor not found"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDTO createAppointment(@PathVariable Long patientId, @RequestBody AppointmentDTO appointmentDTO) {
        Optional<Patient> patient = patientService.getPatientById(patientId);
        Optional<User> doctor = userService.getUserById(appointmentDTO.doctorId());



        if (patient.isPresent() && doctor.isPresent()) {
            Appointment appointment = appointmentConverter.convertToEntity(appointmentDTO);
            appointment.setPatient(patient.get());
            appointment.setDoctor(doctor.get());
            Appointment savedAppointment = appointmentService.saveAppointment(appointment);
            return appointmentConverter.convertToDTO(savedAppointment);
        } else {
            throw new ResourceNotFoundException("Patient or Doctor not found");
        }
    }

    /**
     * Retrieves a list of appointments for a specific patient.
     * Access restricted to doctors and administrators.
     *
     * @param patientId ID of the patient.
     * @param pageable Pagination information.
     * @return A paginated list of appointments.
     */
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(
            summary = "Get appointments by patient",
            description = "Retrieves a paginated list of appointments for a specific patient. Requires DOCTOR or ADMIN roles."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointments retrieved successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found"
            )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AppointmentDTO> getAppointmentsByPatient(@PathVariable Long patientId, Pageable pageable) {
        Optional<Patient> patient = patientService.getPatientById(patientId);

        if (patient.isPresent()) {
            return appointmentService.getAllAppointments(pageable)
                    .map(appointmentConverter::convertToDTO);
        } else {
            throw new ResourceNotFoundException("Patient not found");
        }
    }

    /**
     * Updates the status of an existing appointment for a specific patient.
     * Access restricted to doctors and administrators.
     *
     * @param patientId ID of the patient.
     * @param appointmentId ID of the appointment to be updated.
     * @param appointmentDTO Appointment status in the request body.
     * @return The updated appointment.
     */
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(
            summary = "Update appointment status",
            description = "Updates the status of an existing appointment for a specific patient. Requires DOCTOR or ADMIN roles."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment status updated successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment or Patient not found"
            )
    })
    @PutMapping("/{appointmentId}")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentDTO updateAppointmentStatus(@PathVariable Long patientId, @PathVariable Long appointmentId, @RequestBody AppointmentDTO appointmentDTO) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment.isPresent() && appointment.get().getPatient().getId().equals(patientId)) {
            Appointment updatedAppointment = appointment.get();
            updatedAppointment.setStatus(AppointmentStatus.valueOf(appointmentDTO.status()));
            appointmentService.saveAppointment(updatedAppointment);
            return appointmentConverter.convertToDTO(updatedAppointment);
        } else {
            throw new ResourceNotFoundException("Appointment or Patient not found");
        }
    }
}