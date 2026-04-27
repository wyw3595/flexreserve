package com.flexreserve.appointment.controller;

import com.flexreserve.appointment.entity.Appointment;
import com.flexreserve.appointment.entity.dto.ResourceDTO;
import com.flexreserve.appointment.entity.dto.ResourcePageQueryDTO;
import com.flexreserve.appointment.entity.dto.ResourceStatusDTO;
import com.flexreserve.appointment.entity.vo.Resource;
import com.flexreserve.appointment.service.AppointmentService;
import com.flexreserve.common.PageResult;
import com.flexreserve.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "管理员预约管理模块")
@RequiredArgsConstructor
public class AdminAppointmentController {
    private final AppointmentService appointmentService;

    /**
     * 添加资源
     * @param resourceDTO
     * @return
     */
    @Operation(summary = "添加资源")
    @PostMapping("/resource")
    public Result<Long> addResource(@Valid @RequestBody ResourceDTO resourceDTO){
      Long resourceId =  appointmentService.addResource(resourceDTO);
        return Result.success("添加成功",resourceId );
    }
    @Operation(summary = "更新资源")
    @PutMapping("/resource")
    public Result<String> updateResource(@Valid @RequestBody ResourceDTO resourceDTO){

        return appointmentService.updateResource(resourceDTO);
    }
    @Operation(summary = "停用/启用资源")
    @PutMapping("/resource/{resourceId}/status")
    public Result<Void> updateResourceStatus(
            @PathVariable Long resourceId,
            @Valid @RequestBody ResourceStatusDTO statusDTO){
        return appointmentService.updateResourceStatus(resourceId, statusDTO.getStatus());
    }
    @Operation(summary = "逻辑删除资源")
    @DeleteMapping("/resource/{resourceId}")
    public Result<Void> deleteResource(@PathVariable Long resourceId){
        return appointmentService.deleteResource(resourceId);
    }
    @Operation(summary = "获取资源分页列表")
    @GetMapping("/resource/page")
    public Result<PageResult<ResourceDTO>> getResourcePageList( ResourcePageQueryDTO queryDTO){
        PageResult<ResourceDTO> pageResult = appointmentService.getResourcePageList(queryDTO);
        return Result.success(pageResult);
    }


}
