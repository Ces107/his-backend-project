package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.Gender;

import java.util.Date;

public record CreatePatientDTO(Date dateOfBirth, Gender gender,Long userId){}

