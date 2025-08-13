package com.healthfirst.server.validation;

import com.healthfirst.server.dto.ClinicAddressDto;
import com.healthfirst.server.dto.ProviderRegistrationRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProviderRegistrationRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    private ProviderRegistrationRequest validRequest() {
        return ProviderRegistrationRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phoneNumber("+12345678901")
            .password("StrongPass!1")
            .specialization("Cardiology")
            .licenseNumber("LIC12345")
            .yearsOfExperience(10)
            .clinicAddress(ClinicAddressDto.builder()
                .street("123 Main St")
                .city("Pune")
                .state("MH")
                .zip("411001")
                .build())
            .build();
    }

    @Test
    void validRequest_shouldPass() {
        Set<ConstraintViolation<ProviderRegistrationRequest>> violations = validator.validate(validRequest());
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidPassword_shouldFail() {
        ProviderRegistrationRequest req = validRequest();
        req.setPassword("weakpass");
        Set<ConstraintViolation<ProviderRegistrationRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void invalidPhone_shouldFail() {
        ProviderRegistrationRequest req = validRequest();
        req.setPhoneNumber("12345");
        Set<ConstraintViolation<ProviderRegistrationRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void yearsOfExperience_outOfRange_shouldFail() {
        ProviderRegistrationRequest req = validRequest();
        req.setYearsOfExperience(99);
        Set<ConstraintViolation<ProviderRegistrationRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }
} 