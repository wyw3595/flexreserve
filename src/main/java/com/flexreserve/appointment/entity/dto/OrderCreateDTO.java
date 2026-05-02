
package com.flexreserve.appointment.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "创建预约订单DTO")
public class OrderCreateDTO {

    @NotNull(message = "资源ID不能为空")
    @Schema(description = "要预约的资源ID", example = "1")
    private Long resourceId;

    @NotBlank(message = "预约日期不能为空")
    @Schema(description = "预约日期", example = "2026-05-01")
    private String bookingDate;

    @NotBlank(message = "开始时间不能为空")
    @Schema(description = "时段开始时间", example = "09:00")
    private String startTime;
}
