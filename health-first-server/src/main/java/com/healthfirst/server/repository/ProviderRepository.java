package com.healthfirst.server.repository;

import com.healthfirst.server.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProviderRepository extends JpaRepository<Provider, UUID> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByLicenseNumberIgnoreCase(String licenseNumber);

    Optional<Provider> findByEmailIgnoreCase(String email);
} 