package com.flexreserve.appointment.controller;

import com.flexreserve.appointment.entity.dto.ResourcePageQueryDTO;
import com.flexreserve.appointment.entity.dto.TimeSlotResult;
import com.flexreserve.appointment.entity.vo.ResourceVO;
import com.flexreserve.appointment.service.TimeSlotService;
import com.flexreserve.common.PageResult;
import com.flexreserve.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/resource")
@Tag(name = "用户端资源预约")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @GetMapping("/{resourceId}/slots")
    @Operation(summary = "查看资源可预约时段")
    public Result<List<TimeSlotResult>> getSlots(@PathVariable Long resourceId,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<TimeSlotResult> slots = timeSlotService.getAvailableSlots(resourceId, date);
        return Result.success(slots);
    }
    @GetMapping("/list")
    @Operation(summary = "查看所有资源")
    public Result<PageResult<ResourceVO>> getAllResources(ResourcePageQueryDTO queryDTO) {
        PageResult<ResourceVO> resources = timeSlotService.getAllResources(queryDTO);
        return Result.success(resources);
    }

}