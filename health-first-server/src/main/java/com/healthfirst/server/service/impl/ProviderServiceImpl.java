package com.healthfirst.server.service.impl;

import com.healthfirst.server.dto.ClinicAddressDto;
import com.healthfirst.server.dto.ProviderRegistrationRequest;
import com.healthfirst.server.dto.ProviderResponse;
import com.healthfirst.server.entity.ClinicAddress;
import com.healthfirst.server.entity.Provider;
import com.healthfirst.server.exception.DuplicateResourceException;
import com.healthfirst.server.repository.ProviderRepository;
import com.healthfirst.server.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ProviderResponse registerProvider(ProviderRegistrationRequest request) {
        String email = sanitize(request.getEmail());
        String phone = sanitize(request.getPhoneNumber());
        String license = sanitize(request.getLicenseNumber());

        if (providerRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Email already in use");
        }
        if (providerRepository.existsByPhoneNumber(phone)) {
            throw new DuplicateResourceException("Phone number already in use");
        }
        if (providerRepository.existsByLicenseNumberIgnoreCase(license)) {
            throw new DuplicateResourceException("License number already in use");
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());

        Provider provider = Provider.builder()
            .firstName(sanitize(request.getFirstName()))
            .lastName(sanitize(request.getLastName()))
            .email(email)
            .phoneNumber(phone)
            .passwordHash(passwordHash)
            .specialization(sanitize(request.getSpecialization()))
            .licenseNumber(license)
            .yearsOfExperience(request.getYearsOfExperience())
            .clinicAddress(mapAddress(request.getClinicAddress()))
            .verificationStatus("pending")
            .isActive(true)
            .build();

        Provider saved = providerRepository.save(provider);
        return mapToResponse(saved);
    }

    private String sanitize(String input) {
        return input == null ? null : input.trim();
    }

    private ClinicAddress mapAddress(ClinicAddressDto dto) {
        if (dto == null) return null;
        return ClinicAddress.builder()
            .street(sanitize(dto.getStreet()))
            .city(sanitize(dto.getCity()))
            .state(sanitize(dto.getState()))
            .zip(sanitize(dto.getZip()))
            .build();
    }

    private ProviderResponse mapToResponse(Provider provider) {
        ClinicAddressDto addressDto = null;
        if (provider.getClinicAddress() != null) {
            addressDto = ClinicAddressDto.builder()
                .street(provider.getClinicAddress().getStreet())
                .city(provider.getClinicAddress().getCity())
                .state(provider.getClinicAddress().getState())
                .zip(provider.getClinicAddress().getZip())
                .build();
        }
        return ProviderResponse.builder()
            .id(provider.getId())
            .firstName(provider.getFirstName())
            .lastName(provider.getLastName())
            .email(provider.getEmail())
            .phoneNumber(provider.getPhoneNumber())
            .specialization(provider.getSpecialization())
            .licenseNumber(provider.getLicenseNumber())
            .yearsOfExperience(provider.getYearsOfExperience())
            .clinicAddress(addressDto)
            .verificationStatus(provider.getVerificationStatus())
            .isActive(provider.getIsActive())
            .createdAt(provider.getCreatedAt())
            .updatedAt(provider.getUpdatedAt())
            .build();
    }
} 