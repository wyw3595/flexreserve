

package com.flexreserve.appointment.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "资源分页查询参数")
public class ResourcePageQueryDTO {

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer size = 10;

    @Schema(description = "资源类型", example = "room")
    private String type;

    @Schema(description = "资源状态：0-停用，1-启用", example = "1")
    private Integer status;

    @Schema(hidden = true)
    private Integer offset;
}
