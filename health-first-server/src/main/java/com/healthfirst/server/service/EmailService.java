package com.healthfirst.server.service;

public interface EmailService {
    void sendVerificationEmail(String email, String patientId);
} 