package com.flexreserve.appointment.entity;

import lombok.Data;
import java.time.LocalTime;

@Data
public class BookingConfig {
    private Long id;
    private Long resourceId;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDuration;          // 单位：分钟
    private Integer maxBookingPerSlot;
    private Integer advanceDays;
    private Integer cancelLimitMinutes;
}