package com.healthfirst.server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientLoginRequest {
    @NotBlank @Email private String email;
    @NotBlank private String password;
} 