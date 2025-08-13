package com.healthfirst.server.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRegistrationRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "Phone must be in international format e.g. +123456789")
    private String phoneNumber;

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
        message = "Password must be at least 8 chars and include upper, lower, number, special"
    )
    private String password;

    @NotBlank
    @Size(min = 3, max = 100)
    private String specialization;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "License must be alphanumeric")
    private String licenseNumber;

    @Min(0)
    @Max(50)
    private Integer yearsOfExperience;

    @Valid
    @NotNull
    private ClinicAddressDto clinicAddress;
} 