package com.healthfirst.server.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    @Test
    void generateAndParseToken_shouldContainClaims() {
        JwtService jwtService = new JwtService("test-secret-which-is-long-enough-1234567890", 3600);
        String token = jwtService.generateToken("john.doe@clinic.com", java.util.Map.of(
            "provider_id", "uuid-123",
            "email", "john.doe@clinic.com",
            "role", "PROVIDER",
            "specialization", "Cardiology"
        ));

        Claims claims = jwtService.parseToken(token);
        assertEquals("uuid-123", claims.get("provider_id"));
        assertEquals("john.doe@clinic.com", claims.get("email"));
        assertEquals("PROVIDER", claims.get("role"));
        assertEquals("Cardiology", claims.get("specialization"));
        assertNotNull(claims.getExpiration());
    }
} 