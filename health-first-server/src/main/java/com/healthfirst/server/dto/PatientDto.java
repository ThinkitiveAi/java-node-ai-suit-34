package com.healthfirst.server.dto;

import com.healthfirst.server.entity.PatientGender;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private PatientGender gender;
    private String phoneNumber;
} 