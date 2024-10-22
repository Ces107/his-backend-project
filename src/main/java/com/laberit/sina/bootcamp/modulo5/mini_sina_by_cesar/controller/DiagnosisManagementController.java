package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.controller;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.DiagnosisDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.converters.DiagnosisConverter;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.exception.ResourceNotFoundException;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Diagnosis;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.Patient;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.DiagnosisStatus;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.DiagnosisService;
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
@RequestMapping("mini-sina/v1/patients/{patientId}/diagnoses")
@Tag(
        name = "DiagnosisManagementController",
        description = "Controller for managing diagnoses related to a specific patient."
)
public class DiagnosisManagementController {

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DiagnosisConverter diagnosisConverter;

    /**
     * Creates a new diagnosis for a specific patient.
     *
     * @param patientId ID of the patient.
     * @param diagnosisDTO Diagnosis details in the request body.
     * @return The created diagnosis.
     */
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(
            summary = "Create new diagnosis",
            description = "Creates a new diagnosis for a specific patient."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Diagnosis created successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiagnosisDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DiagnosisDTO createDiagnosis(@PathVariable Long patientId, @RequestBody DiagnosisDTO diagnosisDTO) {
        Optional<Patient> patient = patientService.getPatientById(patientId);
        if (patient.isPresent()) {
            Diagnosis diagnosis = diagnosisConverter.convertToEntity(diagnosisDTO);
            diagnosis.setPatient(patient.get());
            Diagnosis savedDiagnosis = diagnosisService.saveDiagnosis(diagnosis);
            return diagnosisConverter.convertToDTO(savedDiagnosis);
        } else {
            throw new ResourceNotFoundException("Patient not found");
        }
    }

    /**
     * Retrieves diagnoses for a specific patient, with pagination handled via query parameters.
     *
     * @param patientId ID of the patient.
     * @param page Page number for pagination.
     * @param size Page size for pagination.
     * @param sortField Field to sort by.
     * @param sortDirection Sort direction (asc or desc).
     * @return A list of diagnoses.
     */
    @Operation(
            summary = "Get diagnoses by patient",
            description = "Retrieves diagnoses for a specific patient, with optional pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Diagnoses retrieved successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiagnosisDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found"
            )
    })
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DiagnosisDTO> getDiagnosesByPatient(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Optional<Patient> patient = patientService.getPatientById(patientId);

        if (patient.isPresent()) {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
            return diagnosisService.getDiagnosesByPatient(patientId, pageable)
                    .stream()
                    .map(diagnosisConverter::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException("Patient not found");
        }
    }

    /**
     * Updates the status of an existing diagnosis for a specific patient.
     *
     * @param patientId ID of the patient.
     * @param diagnosisId ID of the diagnosis to be updated.
     * @param diagnosisDTO Diagnosis status in the request body.
     * @return The updated diagnosis.
     */
    @Operation(
            summary = "Update diagnosis status",
            description = "Updates the status of an existing diagnosis for a specific patient."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Diagnosis status updated successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiagnosisDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Diagnosis or Patient not found"
            )
    })
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @PutMapping("/{diagnosisId}")
    @ResponseStatus(HttpStatus.OK)
    public DiagnosisDTO updateDiagnosisStatus(
            @PathVariable Long patientId,
            @PathVariable Long diagnosisId,
            @RequestBody DiagnosisDTO diagnosisDTO) {

        Optional<Diagnosis> diagnosis = diagnosisService.getDiagnosisById(diagnosisId);

        if (diagnosis.isPresent() && diagnosis.get().getPatient().getId().equals(patientId)) {
            Diagnosis updatedDiagnosis = diagnosis.get();
            updatedDiagnosis.setStatus(DiagnosisStatus.valueOf(diagnosisDTO.status()));
            diagnosisService.saveDiagnosis(updatedDiagnosis);
            return diagnosisConverter.convertToDTO(updatedDiagnosis);
        } else {
            throw new ResourceNotFoundException("Diagnosis or Patient not found");
        }
    }
}
