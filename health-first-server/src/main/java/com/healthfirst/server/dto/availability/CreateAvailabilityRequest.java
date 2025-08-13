package com.healthfirst.server.dto.availability;

import com.healthfirst.server.entity.availability.AppointmentType;
import com.healthfirst.server.entity.availability.LocationType;
import com.healthfirst.server.entity.availability.RecurrencePattern;
import jakarta.validation.constraints.*;
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
public class CreateAvailabilityRequest {

    @NotNull private UUID providerId;
    @NotNull private LocalDate date;
    @NotNull private LocalTime startTime;
    @NotNull private LocalTime endTime;
    @NotBlank private String timezone;

    private boolean isRecurring;
    private RecurrencePattern recurrencePattern;
    private LocalDate recurrenceEndDate;

    @Min(5) @Max(240) private Integer slotDuration = 30;
    @Min(0) @Max(60) private Integer breakDuration = 0;

    private AppointmentType appointmentType = AppointmentType.consultation;

    private LocationType locationType;
    private String locationAddress;
    private String locationRoomNumber;
    private BigDecimal baseFee;
    private Boolean insuranceAccepted;
    private String currency = "USD";
    @Size(max = 500) private String notes;
} 