package com.healthfirst.server.repository.availability;

import com.healthfirst.server.entity.availability.AppointmentSlot;
import com.healthfirst.server.entity.availability.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, UUID> {

    @Query("select s from AppointmentSlot s where s.providerId = :providerId and s.slotStartTime between :from and :to")
    List<AppointmentSlot> findByProviderAndRange(UUID providerId, OffsetDateTime from, OffsetDateTime to);

    List<AppointmentSlot> findByStatus(AvailabilityStatus status);
} 