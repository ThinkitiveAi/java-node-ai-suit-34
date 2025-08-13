package com.healthfirst.server.service;

import com.healthfirst.server.dto.ClinicAddressDto;
import com.healthfirst.server.dto.ProviderRegistrationRequest;
import com.healthfirst.server.entity.Provider;
import com.healthfirst.server.exception.DuplicateResourceException;
import com.healthfirst.server.repository.ProviderRepository;
import com.healthfirst.server.service.impl.ProviderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProviderServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProviderServiceImpl providerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder(12);
        providerService = new ProviderServiceImpl(providerRepository, passwordEncoder);
    }

    private ProviderRegistrationRequest validRequest() {
        return ProviderRegistrationRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phoneNumber("+12345678901")
            .password("StrongPass!1")
            .specialization("Cardiology")
            .licenseNumber("LIC12345")
            .yearsOfExperience(10)
            .clinicAddress(ClinicAddressDto.builder().street("123").city("Pune").state("MH").zip("411001").build())
            .build();
    }

    @Test
    void duplicateEmail_shouldThrow() {
        when(providerRepository.existsByEmailIgnoreCase("john.doe@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> providerService.registerProvider(validRequest()));
        verify(providerRepository, never()).save(any());
    }

    @Test
    void duplicatePhone_shouldThrow() {
        when(providerRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
        when(providerRepository.existsByPhoneNumber("+12345678901")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> providerService.registerProvider(validRequest()));
        verify(providerRepository, never()).save(any());
    }

    @Test
    void duplicateLicense_shouldThrow() {
        when(providerRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
        when(providerRepository.existsByPhoneNumber(any())).thenReturn(false);
        when(providerRepository.existsByLicenseNumberIgnoreCase("LIC12345")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> providerService.registerProvider(validRequest()));
        verify(providerRepository, never()).save(any());
    }

    @Test
    void passwordShouldBeHashed() {
        when(providerRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
        when(providerRepository.existsByPhoneNumber(any())).thenReturn(false);
        when(providerRepository.existsByLicenseNumberIgnoreCase(any())).thenReturn(false);
        when(providerRepository.save(any(Provider.class))).thenAnswer(invocation -> invocation.getArgument(0));

        providerService.registerProvider(validRequest());

        ArgumentCaptor<Provider> captor = ArgumentCaptor.forClass(Provider.class);
        verify(providerRepository).save(captor.capture());
        Provider saved = captor.getValue();
        assertNotEquals("StrongPass!1", saved.getPasswordHash());
        assertTrue(passwordEncoder.matches("StrongPass!1", saved.getPasswordHash()));
    }
} 