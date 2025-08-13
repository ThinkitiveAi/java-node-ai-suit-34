package com.healthfirst.server.controller;

import com.healthfirst.server.dto.ProviderRegistrationRequest;
import com.healthfirst.server.dto.ProviderResponse;
import com.healthfirst.server.service.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping("/register")
    public ResponseEntity<ProviderResponse> register(@Valid @RequestBody ProviderRegistrationRequest request) {
        ProviderResponse response = providerService.registerProvider(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
} 