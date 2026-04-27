package com.flexreserve.appointment.service;

import com.flexreserve.appointment.entity.Appointment;
import com.flexreserve.appointment.entity.dto.ResourceDTO;
import com.flexreserve.appointment.entity.dto.ResourcePageQueryDTO;
import com.flexreserve.common.PageResult;
import com.flexreserve.common.Result;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public interface AppointmentService {

    Long addResource(ResourceDTO resourceDTO);

    Result<String> updateResource(ResourceDTO resourceDTO);

    Result<Void> updateResourceStatus(Long resourceId, @NotNull(message = "状态不能为空") Integer status);

    Result<Void> deleteResource(Long resourceId);

    PageResult<ResourceDTO> getResourcePageList(ResourcePageQueryDTO queryDTO);
}
