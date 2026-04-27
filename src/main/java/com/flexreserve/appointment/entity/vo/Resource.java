package com.flexreserve.appointment.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
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

    // 逻辑删除标识（0-未删除，1-已删除）
    private Integer isDeleted;

    // 审计字段 - 创建时间
    private LocalDateTime createTime;

    // 审计字段 - 更新时间
    private LocalDateTime updateTime;
}
