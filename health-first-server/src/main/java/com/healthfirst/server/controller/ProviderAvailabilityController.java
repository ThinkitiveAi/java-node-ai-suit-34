package com.healthfirst.server.controller;

import com.healthfirst.server.dto.availability.AvailabilityResponse;
import com.healthfirst.server.dto.availability.CreateAvailabilityRequest;
import com.healthfirst.server.service.availability.ProviderAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProviderAvailabilityController {

    private final ProviderAvailabilityService service;

    @PostMapping("/provider/availability")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody CreateAvailabilityRequest request) {
        AvailabilityResponse res = service.createAvailability(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "success", true,
            "message", "Availability created",
            "data", res
        ));
    }

    @GetMapping("/provider/{providerId}/availability")
    public ResponseEntity<Map<String, Object>> get(
        @PathVariable UUID providerId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
        @RequestParam(required = false) String status,
        @RequestParam(required = false, name = "appointment_type") String appointmentType,
        @RequestParam(required = false) String timezone
    ) {
        List<AvailabilityResponse> list = service.getAvailability(providerId, start_date, end_date, status, appointmentType, timezone);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Availability fetched",
            "data", list
        ));
    }
} 