package com.healthfirst.server.service;

import com.healthfirst.server.dto.PatientLoginRequest;
import com.healthfirst.server.dto.PatientLoginResponse;

public interface PatientAuthService {
    PatientLoginResponse login(PatientLoginRequest request);
} 