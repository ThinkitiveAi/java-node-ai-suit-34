package com.healthfirst.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationSeconds;

    public JwtService(
        @Value("${security.jwt.secret}") String base64OrRawSecret,
        @Value("${security.jwt.expirationSeconds:3600}") long expirationSeconds
    ) {
        SecretKey key;
        try {
            byte[] keyBytes = Decoders.BASE64.decode(base64OrRawSecret);
            key = Keys.hmacShaKeyFor(keyBytes);
        } catch (RuntimeException e) {
            key = Keys.hmacShaKeyFor(base64OrRawSecret.getBytes());
        }
        this.signingKey = key;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
            .subject(subject)
            .claims(claims)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(expirationSeconds)))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateTokenWithExpiry(String subject, Map<String, Object> claims, Duration expiry) {
        Instant now = Instant.now();
        return Jwts.builder()
            .subject(subject)
            .claims(claims)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expiry)))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
} 