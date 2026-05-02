package com.flexreserve.appointment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeSlotResult {
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer totalCapacity;
    private Integer bookedCount;
    private boolean available;
}