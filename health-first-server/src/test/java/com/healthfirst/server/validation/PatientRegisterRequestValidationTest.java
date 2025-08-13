package com.healthfirst.server.validation;

import com.healthfirst.server.dto.PatientRegisterRequest;
import com.healthfirst.server.entity.PatientGender;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatientRegisterRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() { factory.close(); }

    private PatientRegisterRequest valid() {
        return PatientRegisterRequest.builder()
            .firstName("Jane").lastName("Smith")
            .email("jane.smith@email.com")
            .phoneNumber("+12345678901")
            .password("SecurePassword123!")
            .confirmPassword("SecurePassword123!")
            .dateOfBirth(LocalDate.now().minusYears(20))
            .gender(PatientGender.FEMALE)
            .addressStreet("456 Main Street")
            .addressCity("Boston")
            .addressState("MA")
            .addressZip("02101")
            .medicalHistory(List.of("Diabetes"))
            .build();
    }

    @Test
    void validRequest_shouldPass() {
        assertTrue(validator.validate(valid()).isEmpty());
    }

    @Test
    void underAge_shouldFail() {
        PatientRegisterRequest r = valid();
        r.setDateOfBirth(LocalDate.now().minusYears(10));
        assertFalse(validator.validate(r).isEmpty());
    }

    @Test
    void passwordMismatch_shouldFail() {
        PatientRegisterRequest r = valid();
        r.setConfirmPassword("OtherPass!1");
        assertFalse(validator.validate(r).isEmpty());
    }
} 