package com.flexreserve.appointment.service;

import com.flexreserve.appointment.entity.Appointment;
import com.flexreserve.appointment.entity.dto.OrderCreateDTO;
import com.flexreserve.appointment.entity.dto.OrderPageQueryDTO;
import com.flexreserve.appointment.entity.dto.OrderResultDTO;
import com.flexreserve.appointment.entity.vo.OrderPageQueryVO;
import com.flexreserve.appointment.entity.vo.OrderVO;
import com.flexreserve.common.PageResult;
import com.flexreserve.common.Result;
import jakarta.validation.Valid;

public interface OrderService {
    Result<OrderResultDTO> createOrder(@Valid OrderCreateDTO orderCreateDTO);

    Result<PageResult<OrderPageQueryVO>> queryMyOrders(@Valid OrderPageQueryDTO queryDTO);

    Result<String> cancelOrder(String id);

    Result<PageResult<OrderPageQueryVO>> queryAdminOrders(@Valid OrderPageQueryDTO queryDTO, Long resourceId);
}
