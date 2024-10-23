package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Objects;

@Getter
@Setter
@Schema(description = "Details of a diagnosis: id, disease, status, and patient ID.")
public final class DiagnosisDTO {

    private final Long id;

    @Schema(description = "ID of the patient", example = "1")
    private Long patientId;

    @Schema(description = "Disease diagnosed", example = "FEVER",
            allowableValues = {"FEVER", "DIABETES", "HEART_FAILURE"})
    private String disease;

    @Schema(description = "Status of the diagnosis", example = "PENDING",
            allowableValues = {"PENDING", "CONFIRMED", "REJECTED"})
    private String status;

    public DiagnosisDTO(Long id, String disease, String status, Long patientId) {
        this.id = id;
        this.disease = disease;
        this.status = status;
        this.patientId = patientId;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DiagnosisDTO) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.disease, that.disease) &&
                Objects.equals(this.status, that.status) &&
                Objects.equals(this.patientId, that.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, disease, status, patientId);
    }

    @Override
    public String toString() {
        return "DiagnosisDTO[" +
                "id=" + id + ", " +
                "disease=" + disease + ", " +
                "status=" + status + ", " +
                "patientId=" + patientId + ']';
    }

}
