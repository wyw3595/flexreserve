package com.flexreserve.appointment.service;

import com.flexreserve.appointment.entity.Tenant;
import com.flexreserve.common.PageResult;

public interface TenantService {
    PageResult<Tenant> pageQuery(Long current, Long size);
}
