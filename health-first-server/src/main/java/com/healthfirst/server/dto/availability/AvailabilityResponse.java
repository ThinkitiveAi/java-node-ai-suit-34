package com.healthfirst.server.dto.availability;

import com.healthfirst.server.entity.availability.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponse {
    private UUID id;
    private UUID providerId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String timezone;
    private boolean recurring;
    private RecurrencePattern recurrencePattern;
    private LocalDate recurrenceEndDate;
    private Integer slotDuration;
    private Integer breakDuration;
    private AvailabilityStatus status;
    private Integer maxAppointmentsPerSlot;
    private Integer currentAppointments;
    private AppointmentType appointmentType;
    private LocationType locationType;
    private String locationAddress;
    private String locationRoomNumber;
    private BigDecimal baseFee;
    private Boolean insuranceAccepted;
    private String currency;
    private String notes;
    private int slotsGenerated;
} 