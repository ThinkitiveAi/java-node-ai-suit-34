package com.healthfirst.server.service.impl;

import com.healthfirst.server.dto.*;
import com.healthfirst.server.entity.Patient;
import com.healthfirst.server.repository.PatientRepository;
import com.healthfirst.server.security.JwtService;
import com.healthfirst.server.service.PatientAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PatientAuthServiceImpl implements PatientAuthService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public PatientLoginResponse login(PatientLoginRequest request) {
        Patient patient = patientRepository.findByEmailIgnoreCase(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), patient.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("patient_id", patient.getId().toString());
        claims.put("email", patient.getEmail());
        claims.put("role", "PATIENT");

        String token = jwtService.generateTokenWithExpiry(patient.getId().toString(), claims, Duration.ofMinutes(30));

        PatientDto dto = PatientDto.builder()
            .id(patient.getId())
            .email(patient.getEmail())
            .firstName(patient.getFirstName())
            .lastName(patient.getLastName())
            .gender(patient.getGender())
            .phoneNumber(patient.getPhoneNumber())
            .build();

        PatientLoginResponse.DataPayload data = PatientLoginResponse.DataPayload.builder()
            .access_token(token)
            .expires_in(1800)
            .token_type("Bearer")
            .patient(dto)
            .build();

        return PatientLoginResponse.builder()
            .success(true)
            .message("Login successful")
            .data(data)
            .build();
    }
} 