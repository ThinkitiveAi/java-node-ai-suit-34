package com.healthfirst.server.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "providers",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_provider_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_provider_phone", columnNames = {"phone_number"}),
        @UniqueConstraint(name = "uk_provider_license", columnNames = {"license_number"})
    }
)
public class Provider {

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
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "Phone must be in international format e.g. +123456789")
    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @NotBlank
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "License must be alphanumeric")
    @Column(name = "license_number", nullable = false, unique = true, length = 100)
    private String licenseNumber;

    @Min(0)
    @Max(50)
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Valid
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "clinic_street", length = 200, nullable = false)),
        @AttributeOverride(name = "city", column = @Column(name = "clinic_city", length = 100, nullable = false)),
        @AttributeOverride(name = "state", column = @Column(name = "clinic_state", length = 50, nullable = false)),
        @AttributeOverride(name = "zip", column = @Column(name = "clinic_zip", length = 10, nullable = false))
    })
    private ClinicAddress clinicAddress;

    @NotBlank
    @Pattern(regexp = "pending|verified|rejected")
    @Column(name = "verification_status", nullable = false, length = 20)
    private String verificationStatus = "pending";

    @NotBlank
    @Pattern(regexp = "PROVIDER|ADMIN")
    @Column(name = "role", nullable = false, length = 30)
    private String role = "PROVIDER";

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
        if (this.verificationStatus == null) {
            this.verificationStatus = "pending";
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
} 