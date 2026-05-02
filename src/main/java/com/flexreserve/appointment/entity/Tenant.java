package com.flexreserve.appointment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_tenant")
public class Tenant {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String code;

    private String logo;

    private String description;

    private String phone;

    private String address;

    private String website;

    private String businessHours;

    private Integer status;

    private LocalDateTime createTime;

}
