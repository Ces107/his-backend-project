package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.Gender;

import java.util.Date;

public record PatientDTO(Long id, String firstName, String lastName, Date dateOfBirth, Gender gender) {
}
