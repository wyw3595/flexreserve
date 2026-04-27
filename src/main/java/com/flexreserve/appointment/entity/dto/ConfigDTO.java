package com.flexreserve.appointment.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "资源配置详情")
public  class ConfigDTO {

    private Long resourceId;
    @NotBlank(message = "开始时间不能为空")
    @Schema(description = "开始时间", example = "09:00")
    private String startTime;

    @NotBlank(message = "结束时间不能为空")
    @Schema(description = "结束时间", example = "18:00")
    private String endTime;

    @NotNull(message = "时间段时长不能为空")
    @Min(value = 1, message = "时间段时长至少为1分钟")
    @Schema(description = "时间段时长（分钟）", example = "60")
    private Integer slotDuration;

    @NotNull(message = "每时段最大预约数不能为空")
    @Min(value = 1, message = "每时段最大预约数至少为1")
    @Schema(description = "每时段最大预约数", example = "1")
    private Integer maxBookingPerSlot;

    @NotNull(message = "提前预约天数不能为空")
    @Min(value = 0, message = "提前预约天数不能为负数")
    @Schema(description = "可提前预约天数", example = "7")
    private Integer advanceDays;

    @NotNull(message = "取消限制时间不能为空")
    @Min(value = 0, message = "取消限制时间不能为负数")
    @Schema(description = "取消预约限制时间（分钟）", example = "60")
    private Integer cancelLimitMinutes;
}
