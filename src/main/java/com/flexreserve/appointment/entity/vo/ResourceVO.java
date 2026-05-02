package com.flexreserve.appointment.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceVO {
    // 资源ID
    private Long id;

    // 资源名称
    private String name;

    // 资源类型（如会议室、实验室、设备等）
    private String type;

    // 资源描述
    private String description;

    // 位置信息
    private String location;

    // 容量
    private Integer capacity;

    // 状态（0-禁用，1-启用）
    private Integer status;

}
