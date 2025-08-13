package com.healthfirst.server.service;

import com.healthfirst.server.dto.ProviderRegistrationRequest;
import com.healthfirst.server.dto.ProviderResponse;

public interface ProviderService {
    ProviderResponse registerProvider(ProviderRegistrationRequest request);
} 