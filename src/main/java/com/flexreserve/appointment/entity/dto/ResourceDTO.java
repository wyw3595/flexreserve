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
@Schema(description = "资源创建/更新DTO")
public class ResourceDTO {


    private Long id;
    private Long status;
    @NotBlank(message = "资源名称不能为空")
    @Schema(description = "资源名称", example = "一号会议室")
    private String name;

    @NotBlank(message = "资源类型不能为空")
    @Schema(description = "资源类型", example = "room")
    private String type;

    @Schema(description = "资源描述", example = "可容纳10人，配有投影仪")
    private String description;

    @Schema(description = "位置信息", example = "三楼A区")
    private String location;

    @NotNull(message = "容量不能为空")
    @Min(value = 1, message = "容量至少为1")
    @Schema(description = "容量", example = "1")
    private Integer capacity;

    @NotNull(message = "配置信息不能为空")
    @Schema(description = "资源配置")
    private ConfigDTO config;
}