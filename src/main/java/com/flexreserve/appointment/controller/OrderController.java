package com.flexreserve.appointment.controller;

import com.flexreserve.appointment.entity.dto.OrderCreateDTO;
import com.flexreserve.appointment.entity.dto.OrderPageQueryDTO;
import com.flexreserve.appointment.entity.dto.OrderResultDTO;
import com.flexreserve.appointment.entity.vo.OrderPageQueryVO;
import com.flexreserve.appointment.entity.vo.OrderVO;
import com.flexreserve.appointment.service.OrderService;
import com.flexreserve.common.PageResult;
import com.flexreserve.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/booking")
@RestController
@Tag(name = "用户端预约模块")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "用户下单")
    public Result<OrderResultDTO> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO){
        return orderService.createOrder(orderCreateDTO);
    }

    @GetMapping("/my")
    @Operation(summary = "分页查询用户订单")
    public Result<PageResult<OrderPageQueryVO>> queryMyOrders(@Valid OrderPageQueryDTO queryDTO){
        return orderService.queryMyOrders(queryDTO);
    }
    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消订单")
    public Result<String> cancelOrder(@PathVariable String id){
        return orderService.cancelOrder(id);
    }


}
