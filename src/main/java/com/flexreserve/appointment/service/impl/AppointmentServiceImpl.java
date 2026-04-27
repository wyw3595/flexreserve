package com.flexreserve.appointment.service.impl;

import com.flexreserve.appointment.entity.Appointment;
import com.flexreserve.appointment.entity.dto.ConfigDTO;
import com.flexreserve.appointment.entity.dto.ResourceDTO;
import com.flexreserve.appointment.entity.dto.ResourcePageQueryDTO;
import com.flexreserve.appointment.mapper.AppointmentMapper;
import com.flexreserve.appointment.service.AppointmentService;
import com.flexreserve.common.PageResult;
import com.flexreserve.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addResource(ResourceDTO resourceDTO) {
        // 1. 添加可预约资源并返回资源ID
        // 1.2.设置状态为禁用
        resourceDTO.setStatus(0L);
        appointmentMapper.addResource(resourceDTO);

        // 2. 添加可预约资源配置
        ConfigDTO config = resourceDTO.getConfig();
        Long resourceId = resourceDTO.getId();
        config.setResourceId(resourceId);
        appointmentMapper.addResourceConfig(config);


        return resourceId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateResource(ResourceDTO resourceDTO) {
        appointmentMapper.updateResource(resourceDTO);
        ConfigDTO config = resourceDTO.getConfig();
        config.setResourceId(resourceDTO.getId());
        appointmentMapper.updateResourceConfig(config);

        return Result.success("修改成功");
    }

    @Override
    public Result<Void> updateResourceStatus(Long resourceId, Integer status) {
        appointmentMapper.updateResourceStatus(resourceId, status);


       /* if (status == 0) {
            appointmentMapper.cancelAppointmentsByResourceId(resourceId);
        }*/

        return Result.success("状态更新成功", null);
    }

    @Override
    public Result<Void> deleteResource(Long resourceId) {
        // 1.查询资源是否存在
        ResourceDTO resource = appointmentMapper.getBookingResourceById(resourceId);
        if (resource == null) {
            return Result.error("资源不存在");
        }
        // 2.逻辑删除此资源
        appointmentMapper.updateResourceIsDelete(resourceId);
        return Result.success("删除成功", null);
    }

    @Override
    public PageResult<ResourceDTO> getResourcePageList(ResourcePageQueryDTO queryDTO) {
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
        PageResult<ResourceDTO> pageResult = new PageResult<>();
        pageResult.setRecords(appointmentMapper.getResourcePageList(queryDTO));
        pageResult.setTotal(total);
        pageResult.setCurrent((long) queryDTO.getPage());
        pageResult.setSize((long) queryDTO.getSize());

        return pageResult;
    }
}
