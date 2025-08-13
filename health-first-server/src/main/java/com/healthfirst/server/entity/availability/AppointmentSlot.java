package com.healthfirst.server.entity.availability;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointment_slots")
public class AppointmentSlot {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(name = "availability_id", nullable = false)
    private UUID availabilityId;

    @NotNull
    @Column(name = "provider_id", nullable = false)
    private UUID providerId;

    @NotNull
    @Column(name = "slot_start_time", nullable = false)
    private OffsetDateTime slotStartTime; // UTC

    @NotNull
    @Column(name = "slot_end_time", nullable = false)
    private OffsetDateTime slotEndTime; // UTC

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private AvailabilityStatus status = AvailabilityStatus.available;

    @Column(name = "patient_id")
    private UUID patientId;

    @Column(name = "appointment_type", length = 30)
    private String appointmentType;

    @Column(name = "booking_reference", length = 100)
    private String bookingReference;
} 