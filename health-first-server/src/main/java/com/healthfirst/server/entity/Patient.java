package com.healthfirst.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_patient_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_patient_phone", columnNames = {"phone_number"})
    }
)
public class Patient {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 160)
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "Invalid E.164 phone format")
    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @NotBlank
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @NotNull
    @Past
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 30)
    private PatientGender gender;

    @NotBlank
    @Size(max = 200)
    @Column(name = "address_street", nullable = false, length = 200)
    private String addressStreet;

    @NotBlank
    @Size(max = 100)
    @Column(name = "address_city", nullable = false, length = 100)
    private String addressCity;

    @NotBlank
    @Size(max = 50)
    @Column(name = "address_state", nullable = false, length = 50)
    private String addressState;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9\\-]{2,10}$", message = "Invalid postal code")
    @Column(name = "address_zip", nullable = false, length = 10)
    private String addressZip;

    @Size(max = 100)
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Pattern(regexp = "^$|^\\+[1-9]\\d{7,14}$", message = "Invalid phone format")
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Size(max = 50)
    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;

    @Column(name = "medical_history")
    private String medicalHistory; // store as JSON string or comma-separated as per DB config

    @Column(name = "insurance_provider", length = 100)
    private String insuranceProvider;

    @Column(name = "insurance_policy_number", length = 100)
    private String insurancePolicyNumber;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "phone_verified", nullable = false)
    private Boolean phoneVerified = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (emailVerified == null) emailVerified = false;
        if (phoneVerified == null) phoneVerified = false;
        if (isActive == null) isActive = true;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @AssertTrue(message = "Must be at least 13 years old")
    public boolean isAgeValid() {
        if (dateOfBirth == null) return true;
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 13;
    }
} 