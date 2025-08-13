package com.healthfirst.server.repository.availability;

import com.healthfirst.server.entity.availability.ProviderAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ProviderAvailabilityRepository extends JpaRepository<ProviderAvailability, UUID> {

    @Query("select a from ProviderAvailability a where a.providerId = :providerId and a.date between :start and :end")
    List<ProviderAvailability> findByProviderIdAndDateRange(UUID providerId, LocalDate start, LocalDate end);

    @Query("select case when count(a)>0 then true else false end from ProviderAvailability a where a.providerId = :providerId and a.date = :date and ((a.startTime < :end and a.endTime > :start))")
    boolean existsOverlapping(UUID providerId, LocalDate date, LocalTime start, LocalTime end);
} 