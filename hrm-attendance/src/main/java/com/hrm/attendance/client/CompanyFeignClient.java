package com.hrm.attendance.client;

import com.hrm.common.entity.Result;
import com.hrm.domain.company.response.DeptListResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 系统微服务远程调用
 */
@FeignClient("hrm-company")
public interface CompanyFeignClient {
    @GetMapping(value = "company/department")
    @ApiOperation(value = "获取某个企业的部门列表")
    public Result<DeptListResult> findAll();
}
