// AnalyticsService.java
package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.PatientAnalyticsDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.AppointmentAnalyticsDTO;

import java.time.LocalDate;
import java.util.Date;

/**
 * Service to provide analytics data, such as patient and appointment statistics.
 */



public interface AnalyticsService {

    /**
     *@param startDate the start date of the period to analyze, formatted as ISO date. Example: 2024-10-22T00:00:00Z
     *@param endDate the end date of the period to analyze, formatted as ISO date. Example: 2024-10-22T00:00:00Z
     */
    PatientAnalyticsDTO getPatientAnalytics(Date startDate, Date endDate);


    /**
     *@param startDate the start date of the period to analyze, formatted as ISO date. Example: 2024-10-22T00:00:00Z
     *@param endDate the end date of the period to analyze, formatted as ISO date. Example: 2024-10-22T00:00:00Z
     */
    AppointmentAnalyticsDTO getAppointmentAnalytics(Date startDate, Date endDate);


}
