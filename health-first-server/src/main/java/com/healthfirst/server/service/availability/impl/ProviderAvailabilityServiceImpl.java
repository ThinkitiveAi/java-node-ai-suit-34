package com.healthfirst.server.service.availability.impl;

import com.healthfirst.server.dto.availability.AvailabilityResponse;
import com.healthfirst.server.dto.availability.CreateAvailabilityRequest;
import com.healthfirst.server.entity.availability.*;
import com.healthfirst.server.exception.DuplicateResourceException;
import com.healthfirst.server.repository.availability.AppointmentSlotRepository;
import com.healthfirst.server.repository.availability.ProviderAvailabilityRepository;
import com.healthfirst.server.service.availability.ProviderAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProviderAvailabilityServiceImpl implements ProviderAvailabilityService {

    private final ProviderAvailabilityRepository availabilityRepository;
    private final AppointmentSlotRepository slotRepository;

    @Override
    @Transactional
    public AvailabilityResponse createAvailability(CreateAvailabilityRequest r) {
        if (!r.getEndTime().isAfter(r.getStartTime())) {
            throw new IllegalArgumentException("end_time must be after start_time");
        }
        if (availabilityRepository.existsOverlapping(r.getProviderId(), r.getDate(), r.getStartTime(), r.getEndTime())) {
            throw new DuplicateResourceException("Overlapping availability for provider");
        }

        ProviderAvailability entity = ProviderAvailability.builder()
            .providerId(r.getProviderId())
            .date(r.getDate())
            .startTime(r.getStartTime())
            .endTime(r.getEndTime())
            .timezone(r.getTimezone())
            .recurring(r.isRecurring())
            .recurrencePattern(r.getRecurrencePattern())
            .recurrenceEndDate(r.getRecurrenceEndDate())
            .slotDuration(r.getSlotDuration())
            .breakDuration(r.getBreakDuration())
            .appointmentType(r.getAppointmentType())
            .locationType(r.getLocationType())
            .locationAddress(r.getLocationAddress())
            .locationRoomNumber(r.getLocationRoomNumber())
            .baseFee(r.getBaseFee())
            .insuranceAccepted(r.getInsuranceAccepted())
            .currency(r.getCurrency())
            .notes(r.getNotes())
            .build();

        ProviderAvailability saved = availabilityRepository.save(entity);
        int slots = generateSlots(saved);

        return map(saved, slots);
    }

    private int generateSlots(ProviderAvailability a) {
        ZoneId zoneId = ZoneId.of(a.getTimezone());
        LocalDate endDate = a.getRecurring() != null && a.getRecurring() && a.getRecurrenceEndDate() != null
            ? a.getRecurrenceEndDate() : a.getDate();
        LocalDate current = a.getDate();
        int count = 0;
        while (!current.isAfter(endDate)) {
            LocalTime start = a.getStartTime();
            LocalTime end = a.getEndTime();
            ZonedDateTime zStart = ZonedDateTime.of(current, start, zoneId);
            ZonedDateTime zEnd = ZonedDateTime.of(current, end, zoneId);
            OffsetDateTime utcStart = zStart.withZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime();
            OffsetDateTime utcEnd = zEnd.withZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime();

            Duration slotDur = Duration.ofMinutes(a.getSlotDuration());
            Duration breakDur = Duration.ofMinutes(a.getBreakDuration());

            OffsetDateTime iterStart = utcStart;
            while (iterStart.isBefore(utcEnd)) {
                OffsetDateTime iterEnd = iterStart.plus(slotDur);
                if (iterEnd.isAfter(utcEnd)) break;
                slotRepository.save(AppointmentSlot.builder()
                    .availabilityId(a.getId())
                    .providerId(a.getProviderId())
                    .slotStartTime(iterStart)
                    .slotEndTime(iterEnd)
                    .status(AvailabilityStatus.available)
                    .appointmentType(a.getAppointmentType().name())
                    .build());
                count++;
                iterStart = iterEnd.plus(breakDur);
            }

            if (a.getRecurring() != null && a.getRecurring()) {
                current = switch (a.getRecurrencePattern()) {
                    case DAILY -> current.plusDays(1);
                    case WEEKLY -> current.plusWeeks(1);
                    case MONTHLY -> current.plusMonths(1);
                    default -> current;
                };
            } else {
                break;
            }
        }
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponse> getAvailability(UUID providerId, LocalDate start, LocalDate end, String status, String appointmentType, String timezone) {
        List<ProviderAvailability> list = availabilityRepository.findByProviderIdAndDateRange(providerId, start, end);
        List<AvailabilityResponse> out = new ArrayList<>();
        for (ProviderAvailability a : list) {
            out.add(map(a, 0));
        }
        return out;
    }

    private AvailabilityResponse map(ProviderAvailability a, int slots) {
        return AvailabilityResponse.builder()
            .id(a.getId())
            .providerId(a.getProviderId())
            .date(a.getDate())
            .startTime(a.getStartTime())
            .endTime(a.getEndTime())
            .timezone(a.getTimezone())
            .recurring(Boolean.TRUE.equals(a.getRecurring()))
            .recurrencePattern(a.getRecurrencePattern())
            .recurrenceEndDate(a.getRecurrenceEndDate())
            .slotDuration(a.getSlotDuration())
            .breakDuration(a.getBreakDuration())
            .status(a.getStatus())
            .maxAppointmentsPerSlot(a.getMaxAppointmentsPerSlot())
            .currentAppointments(a.getCurrentAppointments())
            .appointmentType(a.getAppointmentType())
            .locationType(a.getLocationType())
            .locationAddress(a.getLocationAddress())
            .locationRoomNumber(a.getLocationRoomNumber())
            .baseFee(a.getBaseFee())
            .insuranceAccepted(a.getInsuranceAccepted())
            .currency(a.getCurrency())
            .notes(a.getNotes())
            .slotsGenerated(slots)
            .build();
    }
} 