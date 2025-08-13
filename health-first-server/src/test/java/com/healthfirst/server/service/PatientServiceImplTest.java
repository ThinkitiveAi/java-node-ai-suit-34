package com.healthfirst.server.service;

import com.healthfirst.server.dto.PatientRegisterRequest;
import com.healthfirst.server.entity.Patient;
import com.healthfirst.server.entity.PatientGender;
import com.healthfirst.server.exception.DuplicateResourceException;
import com.healthfirst.server.repository.PatientRepository;
import com.healthfirst.server.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceImplTest {

    @Mock private PatientRepository patientRepository;
    @Mock private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @InjectMocks private PatientServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder(12);
        service = new PatientServiceImpl(patientRepository, passwordEncoder, emailService);
    }

    private PatientRegisterRequest req() {
        return PatientRegisterRequest.builder()
            .firstName("Jane").lastName("Smith").email("jane@email.com").phoneNumber("+12345678901")
            .password("SecurePassword123!").confirmPassword("SecurePassword123!")
            .dateOfBirth(LocalDate.now().minusYears(20))
            .gender(PatientGender.FEMALE)
            .addressStreet("456").addressCity("Boston").addressState("MA").addressZip("02101")
            .build();
    }

    @Test
    void duplicateEmail_throws() {
        when(patientRepository.existsByEmailIgnoreCase("jane@email.com")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.register(req()));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void duplicatePhone_throws() {
        when(patientRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
        when(patientRepository.existsByPhoneNumber("+12345678901")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.register(req()));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void passwordIsHashed_andEmailSent() {
        when(patientRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
        when(patientRepository.existsByPhoneNumber(any())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenAnswer(i -> {
            Patient p = i.getArgument(0);
            if (p.getId() == null) {
                p.setId(java.util.UUID.randomUUID());
            }
            return p;
        });

        service.register(req());

        ArgumentCaptor<Patient> captor = ArgumentCaptor.forClass(Patient.class);
        verify(patientRepository).save(captor.capture());
        Patient saved = captor.getValue();
        assertNotEquals("SecurePassword123!", saved.getPasswordHash());
        assertTrue(passwordEncoder.matches("SecurePassword123!", saved.getPasswordHash()));
        verify(emailService, times(1)).sendVerificationEmail(eq("jane@email.com"), any());
    }
} 