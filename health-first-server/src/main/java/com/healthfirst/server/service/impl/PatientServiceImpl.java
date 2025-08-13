package com.healthfirst.server.service.impl;

import com.healthfirst.server.dto.PatientRegisterRequest;
import com.healthfirst.server.dto.PatientRegisterResponse;
import com.healthfirst.server.entity.Patient;
import com.healthfirst.server.exception.DuplicateResourceException;
import com.healthfirst.server.repository.PatientRepository;
import com.healthfirst.server.service.EmailService;
import com.healthfirst.server.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public PatientRegisterResponse register(PatientRegisterRequest request) {
        String email = sanitize(request.getEmail());
        String phone = sanitize(request.getPhoneNumber());

        if (patientRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Email is already registered");
        }
        if (patientRepository.existsByPhoneNumber(phone)) {
            throw new DuplicateResourceException("Phone number is already registered");
        }

        Patient patient = Patient.builder()
            .firstName(sanitize(request.getFirstName()))
            .lastName(sanitize(request.getLastName()))
            .email(email)
            .phoneNumber(phone)
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .dateOfBirth(request.getDateOfBirth())
            .gender(request.getGender())
            .addressStreet(sanitize(request.getAddressStreet()))
            .addressCity(sanitize(request.getAddressCity()))
            .addressState(sanitize(request.getAddressState()))
            .addressZip(sanitize(request.getAddressZip()))
            .emergencyContactName(sanitize(request.getEmergencyContactName()))
            .emergencyContactPhone(sanitize(request.getEmergencyContactPhone()))
            .emergencyContactRelationship(sanitize(request.getEmergencyContactRelationship()))
            .medicalHistory(request.getMedicalHistory() == null ? null : String.join(",", request.getMedicalHistory()))
            .insuranceProvider(sanitize(request.getInsuranceProvider()))
            .insurancePolicyNumber(sanitize(request.getInsurancePolicyNumber()))
            .build();

        Patient saved = patientRepository.save(patient);
        emailService.sendVerificationEmail(saved.getEmail(), saved.getId().toString());

        return PatientRegisterResponse.builder()
            .patientId(saved.getId().toString())
            .email(saved.getEmail())
            .phoneNumber(saved.getPhoneNumber())
            .emailVerified(Boolean.TRUE.equals(saved.getEmailVerified()))
            .phoneVerified(Boolean.TRUE.equals(saved.getPhoneVerified()))
            .build();
    }

    private String sanitize(String input) { return input == null ? null : input.trim(); }
} 