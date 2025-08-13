package com.healthfirst.server.service.impl;

import com.healthfirst.server.dto.LoginRequest;
import com.healthfirst.server.dto.LoginResponse;
import com.healthfirst.server.dto.ProviderDto;
import com.healthfirst.server.entity.Provider;
import com.healthfirst.server.repository.ProviderRepository;
import com.healthfirst.server.security.JwtService;
import com.healthfirst.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Provider provider = providerRepository.findByEmailIgnoreCase(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), provider.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("provider_id", provider.getId().toString());
        claims.put("email", provider.getEmail());
        claims.put("role", provider.getRole());
        claims.put("specialization", provider.getSpecialization());

        String token = jwtService.generateToken(provider.getEmail(), claims);

        ProviderDto providerDto = ProviderDto.builder()
            .id(provider.getId())
            .email(provider.getEmail())
            .role(provider.getRole())
            .specialization(provider.getSpecialization())
            .firstName(provider.getFirstName())
            .lastName(provider.getLastName())
            .build();

        LoginResponse.DataPayload data = LoginResponse.DataPayload.builder()
            .access_token(token)
            .expires_in(jwtService.getExpirationSeconds())
            .token_type("Bearer")
            .provider(providerDto)
            .build();

        return LoginResponse.builder()
            .success(true)
            .message("Login successful")
            .data(data)
            .build();
    }
} 