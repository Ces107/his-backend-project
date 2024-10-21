package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> accessForDoctor() {
        return ResponseEntity.ok("Hello Doctor! You have access to this endpoint.");
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> accessForManager() {
        return ResponseEntity.ok("Hello Manager! You have access to this endpoint.");
    }
}
