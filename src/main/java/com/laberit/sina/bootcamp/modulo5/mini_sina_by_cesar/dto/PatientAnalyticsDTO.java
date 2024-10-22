// PatientAnalyticsDTO.java
package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class PatientAnalyticsDTO {

    @Schema(description = "Total number of patients categorized by gender", example = "{\"MALE\": 10, \"FEMALE\": 15, \"OTHER\": 2}")
    private Map<String, Long> totalPatientsByGender;
    private Double averageAge;
    private Long patientsWithDiagnoses;

}
