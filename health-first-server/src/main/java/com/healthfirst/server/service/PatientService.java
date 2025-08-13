package com.healthfirst.server.service;

import com.healthfirst.server.dto.PatientRegisterRequest;
import com.healthfirst.server.dto.PatientRegisterResponse;

public interface PatientService {
    PatientRegisterResponse register(PatientRegisterRequest request);
} 