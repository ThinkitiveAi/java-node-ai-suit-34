package com.healthfirst.server.dto;

import com.healthfirst.server.entity.PatientGender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegisterRequest {

    @NotBlank @Size(min = 2, max = 50) private String firstName;
    @NotBlank @Size(min = 2, max = 50) private String lastName;
    @NotBlank @Email private String email;
    @NotBlank @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "Invalid E.164 phone format") private String phoneNumber;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$", message = "Password must include upper, lower, number, special and be >= 8")
    private String password;

    @NotBlank private String confirmPassword;

    @NotNull @Past private LocalDate dateOfBirth;

    @NotNull private PatientGender gender;

    @NotBlank @Size(max = 200) private String addressStreet;
    @NotBlank @Size(max = 100) private String addressCity;
    @NotBlank @Size(max = 50) private String addressState;
    @NotBlank @Pattern(regexp = "^[A-Za-z0-9\\-]{2,10}$", message = "Invalid postal code") private String addressZip;

    @Size(max = 100) private String emergencyContactName;
    @Pattern(regexp = "^$|^\\+[1-9]\\d{7,14}$", message = "Invalid phone format") private String emergencyContactPhone;
    @Size(max = 50) private String emergencyContactRelationship;

    private List<@NotBlank String> medicalHistory;

    @Size(max = 100) private String insuranceProvider;
    @Size(max = 100) private String insurancePolicyNumber;

    @AssertTrue(message = "Must be at least 13 years old")
    public boolean isAgeValid() {
        if (dateOfBirth == null) return true;
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 13;
    }

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordConfirmed() {
        if (password == null || confirmPassword == null) return true;
        return password.equals(confirmPassword);
    }
} 