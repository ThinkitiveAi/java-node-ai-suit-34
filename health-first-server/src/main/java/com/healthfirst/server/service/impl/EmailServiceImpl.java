package com.healthfirst.server.service.impl;

import com.healthfirst.server.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendVerificationEmail(String email, String patientId) {
        log.info("Verification email queued for {} (patientId={})", email, patientId);
    }
} 