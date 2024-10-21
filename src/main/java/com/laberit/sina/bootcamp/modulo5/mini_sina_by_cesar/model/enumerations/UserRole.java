package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations;

import lombok.Getter;


@Getter
public enum UserRole {
    ADMIN("admin"),
    DOCTOR("doctor"),
    MANAGER("manager"),
    PATIENT("patient");

    private final String role;

    UserRole(String role){
        this.role = role;
    }

}