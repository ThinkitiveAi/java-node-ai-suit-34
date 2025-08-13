package com.healthfirst.server.entity.availability;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "provider_availability")
public class ProviderAvailability {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(name = "provider_id", nullable = false)
    private UUID providerId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @NotBlank
    @Column(name = "timezone", nullable = false, length = 100)
    private String timezone;

    @Column(name = "is_recurring", nullable = false)
    private Boolean recurring = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_pattern", length = 20)
    private RecurrencePattern recurrencePattern;

    @Column(name = "recurrence_end_date")
    private LocalDate recurrenceEndDate;

    @Min(5)
    @Max(240)
    @Column(name = "slot_duration", nullable = false)
    private Integer slotDuration = 30;

    @Min(0)
    @Max(60)
    @Column(name = "break_duration", nullable = false)
    private Integer breakDuration = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private AvailabilityStatus status = AvailabilityStatus.available;

    @Min(1)
    @Max(10)
    @Column(name = "max_appointments_per_slot", nullable = false)
    private Integer maxAppointmentsPerSlot = 1;

    @Min(0)
    @Column(name = "current_appointments", nullable = false)
    private Integer currentAppointments = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", length = 30, nullable = false)
    private AppointmentType appointmentType = AppointmentType.consultation;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", length = 30)
    private LocationType locationType;

    @Column(name = "location_address")
    private String locationAddress;

    @Column(name = "location_room_number", length = 50)
    private String locationRoomNumber;

    @Column(name = "base_fee")
    private BigDecimal baseFee;

    @Column(name = "insurance_accepted")
    private Boolean insuranceAccepted;

    @Column(name = "currency", length = 10, nullable = false)
    private String currency = "USD";

    @Size(max = 500)
    @Column(name = "notes")
    private String notes;

    @Column(name = "special_requirements")
    private String specialRequirements; // store as JSON string

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (recurring == null) recurring = false;
        if (status == null) status = AvailabilityStatus.available;
        if (slotDuration == null) slotDuration = 30;
        if (breakDuration == null) breakDuration = 0;
        if (maxAppointmentsPerSlot == null) maxAppointmentsPerSlot = 1;
        if (currentAppointments == null) currentAppointments = 0;
        if (currency == null) currency = "USD";
        if (appointmentType == null) appointmentType = AppointmentType.consultation;
    }

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }
} 