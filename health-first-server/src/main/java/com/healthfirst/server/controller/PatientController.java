package com.healthfirst.server.controller;

import com.healthfirst.server.dto.PatientRegisterRequest;
import com.healthfirst.server.dto.PatientRegisterResponse;
import com.healthfirst.server.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody PatientRegisterRequest request) {
        PatientRegisterResponse res = patientService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "success", true,
            "message", "Patient registered successfully. Verification email sent.",
            "data", res
        ));
    }
} 