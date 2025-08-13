package com.healthfirst.server.service;

import com.healthfirst.server.dto.LoginRequest;
import com.healthfirst.server.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
} 