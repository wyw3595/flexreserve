package com.flexreserve.appointment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    private Long id;
    private Long userId;            // 预约人的 ID (关联 User 表)
    private String targetName;      // 预约的目标名称 (比如：软件工程实验室A区)
    private LocalDateTime appointmentTime; // 预约的具体时间
    private Integer status;         // 状态 (1: 预约成功, 0: 待审核/取消)
    private String remark;          // 备注留言
}
