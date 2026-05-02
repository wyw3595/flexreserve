package com.flexreserve.appointment.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "预约订单VO")
public class OrderResultDTO {

    @Schema(description = "订单编号", example = "a1b2c3d4e5f67890")
    private String orderNo;

    @Schema(description = "订单ID", example = "123")
    private String id;
}
