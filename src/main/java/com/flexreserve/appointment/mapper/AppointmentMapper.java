package com.flexreserve.appointment.mapper;

import com.flexreserve.appointment.entity.Appointment;
import com.flexreserve.appointment.entity.BookingConfig;
import com.flexreserve.appointment.entity.dto.ConfigDTO;
import com.flexreserve.appointment.entity.dto.ResourceDTO;
import com.flexreserve.appointment.entity.dto.ResourcePageQueryDTO;
import com.flexreserve.appointment.entity.vo.ResourceVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.redis.core.convert.RedisData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    /** 根据资源ID查询预约配置 */
    BookingConfig getConfigByResourceId(@Param("resourceId") Long resourceId);

    /** 查询某资源某日期下各时段的已确认订单数量 */

    List<Map<String, Object>> countBookingsBySlot(@Param("resourceId") Long resourceId,
                                                  @Param("bookingDate") LocalDate bookingDate);

    List<ResourceVO> getResourcePageListVO(ResourcePageQueryDTO queryDTO);
}
