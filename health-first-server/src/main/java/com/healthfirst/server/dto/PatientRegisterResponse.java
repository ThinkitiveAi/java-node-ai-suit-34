package com.healthfirst.server.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegisterResponse {
    private String patientId;
    private String email;
    private String phoneNumber;
    private boolean emailVerified;
    private boolean phoneVerified;
} 