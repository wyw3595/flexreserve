package com.flexreserve.appointment.controller;

import com.flexreserve.appointment.entity.Tenant;
import com.flexreserve.appointment.service.TenantService;
import com.flexreserve.common.PageResult;
import com.flexreserve.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
@Tag(name = "租户模块")
public class TenantController {
    private final TenantService tenantService;

    @Operation(summary = "分页获取租户列表")
    @GetMapping("/list")
    public Result<PageResult<Tenant>> pageQuery(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        PageResult<Tenant> pageResult = tenantService.pageQuery(current, size);
        return Result.success(pageResult);
    }


}
