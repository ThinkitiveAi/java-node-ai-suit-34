package com.healthfirst.server.controller;

import com.healthfirst.server.dto.PatientLoginRequest;
import com.healthfirst.server.dto.PatientLoginResponse;
import com.healthfirst.server.service.PatientAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
public class PatientAuthController {

    private final PatientAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<PatientLoginResponse> login(@Valid @RequestBody PatientLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
} 