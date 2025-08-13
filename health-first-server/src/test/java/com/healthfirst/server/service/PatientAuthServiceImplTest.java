package com.healthfirst.server.service;

import com.healthfirst.server.dto.PatientLoginRequest;
import com.healthfirst.server.dto.PatientLoginResponse;
import com.healthfirst.server.entity.Patient;
import com.healthfirst.server.entity.PatientGender;
import com.healthfirst.server.repository.PatientRepository;
import com.healthfirst.server.security.JwtService;
import com.healthfirst.server.service.impl.PatientAuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientAuthServiceImplTest {

    @Mock private PatientRepository patientRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private PatientAuthServiceImpl authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder(12);
        jwtService = new JwtService("test-secret-which-is-long-enough-1234567890", 1800);
        authService = new PatientAuthServiceImpl(patientRepository, passwordEncoder, jwtService);
    }

    private Patient patientWithPassword(String raw) {
        Patient p = new Patient();
        p.setId(UUID.randomUUID());
        p.setEmail("jane.smith@email.com");
        p.setFirstName("Jane");
        p.setLastName("Smith");
        p.setGender(PatientGender.FEMALE);
        p.setPhoneNumber("+12345678901");
        p.setPasswordHash(passwordEncoder.encode(raw));
        return p;
    }

    @Test
    void login_success_returnsTokenAndPatient() {
        Patient p = patientWithPassword("SecurePassword123!");
        when(patientRepository.findByEmailIgnoreCase("jane.smith@email.com")).thenReturn(Optional.of(p));

        PatientLoginRequest req = PatientLoginRequest.builder().email("jane.smith@email.com").password("SecurePassword123!").build();
        PatientLoginResponse res = authService.login(req);

        assertTrue(res.isSuccess());
        assertEquals("Login successful", res.getMessage());
        assertNotNull(res.getData());
        assertNotNull(res.getData().getAccess_token());
        assertEquals(1800, res.getData().getExpires_in());
        assertEquals("Bearer", res.getData().getToken_type());
        assertEquals("jane.smith@email.com", res.getData().getPatient().getEmail());
    }

    @Test
    void login_invalidPassword_throws() {
        Patient p = patientWithPassword("RightPassword!1");
        when(patientRepository.findByEmailIgnoreCase("jane.smith@email.com")).thenReturn(Optional.of(p));

        PatientLoginRequest req = PatientLoginRequest.builder().email("jane.smith@email.com").password("wrong").build();
        assertThrows(BadCredentialsException.class, () -> authService.login(req));
    }

    @Test
    void login_unknownEmail_throws() {
        when(patientRepository.findByEmailIgnoreCase("jane.smith@email.com")).thenReturn(Optional.empty());
        PatientLoginRequest req = PatientLoginRequest.builder().email("jane.smith@email.com").password("whatever").build();
        assertThrows(BadCredentialsException.class, () -> authService.login(req));
    }
} 