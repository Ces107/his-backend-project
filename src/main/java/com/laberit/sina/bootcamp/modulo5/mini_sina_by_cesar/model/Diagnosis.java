package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="diagnosis")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(name = "disease", nullable = false)
    private Disease disease;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DiagnosisStatus status;

    public enum Disease {
        FEVER,
        DIABETES,
        HEART_FAILURE
    }

    public enum DiagnosisStatus {
        PENDING,
        CONFIRMED,
        REJECTED
    }
}