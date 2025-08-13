package com.healthfirst.server.service.availability;

import com.healthfirst.server.dto.availability.AvailabilityResponse;
import com.healthfirst.server.dto.availability.CreateAvailabilityRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProviderAvailabilityService {
    AvailabilityResponse createAvailability(CreateAvailabilityRequest request);
    List<AvailabilityResponse> getAvailability(UUID providerId, LocalDate start, LocalDate end, String status, String appointmentType, String timezone);
} 