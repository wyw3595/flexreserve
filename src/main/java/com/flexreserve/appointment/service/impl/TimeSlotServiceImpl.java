package com.flexreserve.appointment.service.impl;

import com.flexreserve.appointment.entity.BookingConfig;
import com.flexreserve.appointment.entity.dto.ResourcePageQueryDTO;
import com.flexreserve.appointment.entity.dto.TimeSlotResult;
import com.flexreserve.appointment.entity.vo.ResourceVO;
import com.flexreserve.appointment.mapper.AppointmentMapper;
import com.flexreserve.appointment.service.TimeSlotService;
import com.flexreserve.common.PageResult;
import com.flexreserve.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeSlotServiceImpl implements TimeSlotService {

    private final AppointmentMapper appointmentMapper;

    @Override
    public List<TimeSlotResult> getAvailableSlots(Long resourceId, LocalDate date) {
        // 1. 查资源配置
        BookingConfig config = appointmentMapper.getConfigByResourceId(resourceId);
        if (config == null) {
            throw new BusinessException("资源预约配置不存在");
        }

        // 2. 校验日期是否在可预约范围内
        LocalDate today = LocalDate.now();
        if (date.isAfter(today.plusDays(config.getAdvanceDays()))) {
            throw new BusinessException("超出可预约天数");
        }
        // 也可加：禁止预约今天之前的日期
        if (date.isBefore(today)) {
            throw new BusinessException("不可预约过去的时间");
        }

        // 3. 查询已预约数量
        List<Map<String, Object>> bookedList = appointmentMapper.countBookingsBySlot(resourceId, date);
        Map<LocalTime, Integer> bookedCountMap = bookedList.stream()
                .collect(Collectors.toMap(
                        row -> {
                            Object startTime = row.get("start_time");
                            if (startTime instanceof java.sql.Time) {
                                return ((java.sql.Time) startTime).toLocalTime();
                            } else if (startTime instanceof LocalTime) {
                                return (LocalTime) startTime;
                            }
                            throw new BusinessException("时段数据类型异常");
                        },
                        row -> ((Number) row.get("count")).intValue()
                ));

        // 4. 生成时段
        LocalTime slotStart = config.getStartTime();
        LocalTime deadline = config.getEndTime();
        int slotDuration = config.getSlotDuration();
        int maxPerSlot = config.getMaxBookingPerSlot();
        List<TimeSlotResult> slots = new ArrayList<>();

        while (slotStart.plusMinutes(slotDuration).compareTo(deadline) <= 0) {
            LocalTime slotEnd = slotStart.plusMinutes(slotDuration);

            int booked = bookedCountMap.getOrDefault(slotStart, 0);
            boolean availableByCapacity = booked < maxPerSlot;
            boolean isExpired = date.equals(today) && slotStart.isBefore(LocalTime.now());
            boolean available = availableByCapacity && !isExpired;

            TimeSlotResult slot = new TimeSlotResult(
                    slotStart,
                    slotEnd,
                    maxPerSlot,
                    booked,
                    available
            );
            slots.add(slot);
            slotStart = slotEnd;
        }
        return slots;
    }

    @Override
    public PageResult<ResourceVO> getAllResources(ResourcePageQueryDTO queryDTO) {
        // 1.参数校验和默认值处理
        if (queryDTO.getPage() == null || queryDTO.getPage() < 1) {
            queryDTO.setPage(1);
        }
        if (queryDTO.getSize() == null || queryDTO.getSize() < 1) {
            queryDTO.setSize(10);

        }
        // 2.计算偏移量
        int offset = (queryDTO.getPage() - 1) * queryDTO.getSize();
        queryDTO.setOffset(offset);

        // 3.查询总数
        Long total = appointmentMapper.getResourcePageListTotal(queryDTO);
        // 4.查询分页数据
        PageResult<ResourceVO> pageResult = new PageResult<>();
        pageResult.setRecords(appointmentMapper.getResourcePageListVO(queryDTO));
        pageResult.setTotal(total);
        pageResult.setCurrent((long) queryDTO.getPage());
        pageResult.setSize((long) queryDTO.getSize());

        return pageResult;
    }
}