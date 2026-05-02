package com.flexreserve.appointment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flexreserve.appointment.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}
