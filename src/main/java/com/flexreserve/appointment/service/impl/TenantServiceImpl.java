package com.flexreserve.appointment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flexreserve.appointment.entity.Tenant;
import com.flexreserve.appointment.mapper.TenantMapper;
import com.flexreserve.appointment.service.TenantService;
import com.flexreserve.common.PageResult;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    @Override
    public PageResult<Tenant> pageQuery(Long current, Long size) {
        Page<Tenant> page = new Page<>(current, size);
        IPage<Tenant> tenantPage = this.page(page);

        return new PageResult<>(
                tenantPage.getRecords(),
                tenantPage.getTotal(),
                tenantPage.getCurrent(),
                tenantPage.getSize()
        );
    }
}
