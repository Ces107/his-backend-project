package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.controller;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.PatientAnalyticsDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.AppointmentAnalyticsDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/api/analytics")
@Tag(
        name = "AnalyticsController",
        description = "Controller that exposes analytics endpoints for patients and appointments. Supports optional date range filtering."
)
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * Endpoint to get patient analytics with optional date range filtering.
     * Requires the MANAGER role to access.
     *
     * @param startDate Optional start date to filter the analytics.
     * @param endDate Optional end date to filter the analytics.
     * @return Patient analytics data.
     */
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(
            summary = "Get patient analytics",
            description = "Returns analytics data related to patients, optionally filtered by a date range."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient analytics retrieved successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientAnalyticsDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid date format or invalid parameters"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/patients")
    public PatientAnalyticsDTO getPatientAnalytics(
            @Parameter(description = "Start date for filtering", example = "2023-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Parameter(description = "End date for filtering", example = "2023-12-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        return analyticsService.getPatientAnalytics(startDate, endDate);
    }

    /**
     * Endpoint to get appointment analytics with optional date range filtering.
     * Requires the MANAGER role to access.
     *
     * @param startDate Optional start date to filter the analytics.
     * @param endDate Optional end date to filter the analytics.
     * @return Appointment analytics data.
     */


    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(
            summary = "Get appointment analytics",
            description = "Returns analytics data related to appointments, optionally filtered by a date range."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment analytics retrieved successfully"
            )
    })
    @GetMapping("/appointments")
    public AppointmentAnalyticsDTO getAppointmentAnalytics(
            @Parameter(description = "Start date for filtering", example = "2023-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Parameter(description = "End date for filtering", example = "2023-12-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        return analyticsService.getAppointmentAnalytics(startDate, endDate);
    }
}
