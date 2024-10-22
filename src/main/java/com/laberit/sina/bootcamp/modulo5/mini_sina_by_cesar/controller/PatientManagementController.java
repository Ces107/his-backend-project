package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.controller;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.CreatePatientDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.PatientDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.converters.PatientConverter;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.exception.ResourceNotFoundException;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mini-sina/v1/patients")
@Tag(
        name = "PatientManagementController",
        description = "Controller for managing patient data."
)
public class PatientManagementController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientConverter patientConverter;

    /**
     * Creates a new patient.
     *
     * @param patientDTO Patient details in the request body.
     * @return The created patient.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    @Operation(
            summary = "Create new patient",
            description = "Creates a new patient in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Patient created successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class)
                    )}
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDTO createPatient(@RequestBody CreatePatientDTO patientDTO) {
        Patient patient = patientConverter.convertToEntityNoId(patientDTO);
        Patient savedPatient = patientService.savePatient(patient);
        return patientConverter.convertToDTO(savedPatient);
    }

    /**
     * Retrieves all patients, with pagination handled via query parameters.
     *
     * @param page Page number for pagination.
     * @param size Page size for pagination.
     * @param sortField Field to sort by.
     * @param sortDirection Sort direction (asc or desc).
     * @return A list of patients.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    @Operation(
            summary = "Get all patients",
            description = "Retrieves all patients, with optional pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Patients retrieved successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class)
                    )}
            )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PatientDTO> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return patientService.getAllPatients(pageable)
                .stream()
                .map(patientConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a patient by ID.
     *
     * @param patientId ID of the patient.
     * @return The patient with the specified ID.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    @Operation(
            summary = "Get patient by ID",
            description = "Retrieves a patient by their unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient retrieved successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found"
            )
    })
    @GetMapping("/{patientId}")
    @ResponseStatus(HttpStatus.OK)
    public PatientDTO getPatientById(@PathVariable Long patientId) {
        Optional<Patient> patient = patientService.getPatientById(patientId);
        return patient.map(patientConverter::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    /**
     * Updates an existing patient by ID.
     *
     * @param patientId ID of the patient to update.
     * @param patientDTO Updated patient details in the request body.
     * @return The updated patient.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    @Operation(
            summary = "Update patient by ID",
            description = "Updates the details of an existing patient by their unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient updated successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found"
            )
    })
    @PutMapping("/{patientId}")
    @ResponseStatus(HttpStatus.OK)
    public PatientDTO updatePatient(@PathVariable Long patientId, @RequestBody PatientDTO patientDTO) {
        Optional<Patient> existingPatient = patientService.getPatientById(patientId);

        if (existingPatient.isPresent()) {
            Patient patientToUpdate = patientConverter.convertToEntity(patientDTO);
            patientToUpdate.setId(patientId);
            Patient updatedPatient = patientService.savePatient(patientToUpdate);
            return patientConverter.convertToDTO(updatedPatient);
        } else {
            throw new ResourceNotFoundException("Patient not found");
        }
    }
}
