package com.healthfirst.server.service;

import com.healthfirst.server.dto.LoginRequest;
import com.healthfirst.server.dto.LoginResponse;
import com.healthfirst.server.entity.Provider;
import com.healthfirst.server.repository.ProviderRepository;
import com.healthfirst.server.security.JwtService;
import com.healthfirst.server.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthServiceImpl authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder(12);
        jwtService = new JwtService("test-secret-which-is-long-enough-1234567890", 3600);
        authService = new AuthServiceImpl(providerRepository, passwordEncoder, jwtService);
    }

    private Provider providerWithPassword(String rawPassword) {
        Provider p = new Provider();
        p.setId(UUID.randomUUID());
        p.setEmail("john.doe@clinic.com");
        p.setPasswordHash(passwordEncoder.encode(rawPassword));
        p.setRole("PROVIDER");
        p.setSpecialization("Cardiology");
        p.setFirstName("John");
        p.setLastName("Doe");
        return p;
    }

    @Test
    void login_success_returnsTokenAndProvider() {
        Provider p = providerWithPassword("SecurePassword123!");
        when(providerRepository.findByEmailIgnoreCase("john.doe@clinic.com")).thenReturn(Optional.of(p));

        LoginRequest req = LoginRequest.builder().email("john.doe@clinic.com").password("SecurePassword123!").build();
        LoginResponse res = authService.login(req);

        assertTrue(res.isSuccess());
        assertEquals("Login successful", res.getMessage());
        assertNotNull(res.getData());
        assertNotNull(res.getData().getAccess_token());
        assertEquals(3600, res.getData().getExpires_in());
        assertEquals("Bearer", res.getData().getToken_type());
        assertEquals("john.doe@clinic.com", res.getData().getProvider().getEmail());
    }

    @Test
    void login_invalidPassword_throws() {
        Provider p = providerWithPassword("CorrectPassword!1");
        when(providerRepository.findByEmailIgnoreCase("john.doe@clinic.com")).thenReturn(Optional.of(p));

        LoginRequest req = LoginRequest.builder().email("john.doe@clinic.com").password("wrong").build();
        assertThrows(BadCredentialsException.class, () -> authService.login(req));
    }

    @Test
    void login_unknownEmail_throws() {
        when(providerRepository.findByEmailIgnoreCase("john.doe@clinic.com")).thenReturn(Optional.empty());
        LoginRequest req = LoginRequest.builder().email("john.doe@clinic.com").password("whatever").build();
        assertThrows(BadCredentialsException.class, () -> authService.login(req));
    }
} 