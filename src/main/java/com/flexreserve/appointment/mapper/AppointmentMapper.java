package com.flexreserve.appointment.mapper;

import com.flexreserve.appointment.entity.Appointment;
import com.flexreserve.appointment.entity.dto.ConfigDTO;
import com.flexreserve.appointment.entity.dto.ResourceDTO;
import com.flexreserve.appointment.entity.dto.ResourcePageQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.redis.core.convert.RedisData;

import java.util.List;

@Mapper
public interface AppointmentMapper {


    Long addResource(ResourceDTO resourceDTO);

    void addResourceConfig(ConfigDTO config);

    void updateResource(ResourceDTO resourceDTO);

    void updateResourceConfig(ConfigDTO config);

    void updateResourceStatus(Long resourceId, Integer status);

    ResourceDTO getResourceById(Long resourceId);

    ResourceDTO getBookingResourceById(Long resourceId);

    void updateResourceIsDelete(Long resourceId);

    Long getResourcePageListTotal(ResourcePageQueryDTO queryDTO);

    List<ResourceDTO> getResourcePageList(ResourcePageQueryDTO queryDTO);
}
