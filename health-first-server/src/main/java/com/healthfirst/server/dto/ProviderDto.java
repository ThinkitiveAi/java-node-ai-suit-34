package com.healthfirst.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDto {
    private UUID id;
    private String email;
    private String role;
    private String specialization;
    private String firstName;
    private String lastName;
} 