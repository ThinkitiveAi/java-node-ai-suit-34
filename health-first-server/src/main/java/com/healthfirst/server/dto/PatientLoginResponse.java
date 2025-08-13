package com.healthfirst.server.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientLoginResponse {
    private boolean success;
    private String message;
    private DataPayload data;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataPayload {
        private String access_token;
        private long expires_in;
        private String token_type;
        private PatientDto patient;
    }
} 