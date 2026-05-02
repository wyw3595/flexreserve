package com.flexreserve.appointment.service;

import com.flexreserve.appointment.entity.dto.ResourcePageQueryDTO;
import com.flexreserve.appointment.entity.dto.TimeSlotResult;
import com.flexreserve.appointment.entity.vo.ResourceVO;
import com.flexreserve.common.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface TimeSlotService {
    List<TimeSlotResult> getAvailableSlots(Long resourceId, LocalDate date);

    PageResult<ResourceVO> getAllResources(ResourcePageQueryDTO queryDTO);
}