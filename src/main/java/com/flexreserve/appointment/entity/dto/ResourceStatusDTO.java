package com.flexreserve.appointment.entity.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "资源状态更新DTO")
public class ResourceStatusDTO {

    @NotNull(message = "状态不能为空")
    @Schema(description = "资源状态：0-停用，1-启用", example = "0")
    private Integer status;
}
