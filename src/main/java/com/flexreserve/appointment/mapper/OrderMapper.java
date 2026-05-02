package com.flexreserve.appointment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flexreserve.appointment.entity.BookingOrder;
import com.flexreserve.appointment.entity.dto.OrderPageQueryDTO;
import com.flexreserve.appointment.entity.vo.OrderPageQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper extends BaseMapper<BookingOrder> {
    List<OrderPageQueryVO> selectOrderPage(Page<OrderPageQueryVO> page,
                                           @Param("userId") Long userId,
                                           @Param("status") Integer status);

    List<OrderPageQueryVO> selectAdminOrderPage(Page<OrderPageQueryDTO> page, Long resourceId, Integer status);
}
