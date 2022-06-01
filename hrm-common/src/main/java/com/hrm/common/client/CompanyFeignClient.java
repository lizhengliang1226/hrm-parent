package com.hrm.common.client;

import com.hrm.common.entity.Result;
import com.hrm.domain.company.Company;
import com.hrm.domain.company.Department;
import com.hrm.domain.company.response.DeptListResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 调用企业微服务客户端
 *
 * @author LZL
 * @date 2022/3/15-0:18
 */
@FeignClient(name = "hrm-company")
public interface CompanyFeignClient {
    /**
     * 根据部门编码查找部门信息
     *
     * @param code      部门编码
     * @param companyId 企业id
     * @return 部门信息
     */
    @GetMapping(value = "/company/department/code/{code}/{companyId}", name = "FIND_DEPT_CODE_API")
    @ApiOperation(value = "根据部门编码查找部门")
    public Result<Department> findByCode(@PathVariable String code, @PathVariable String companyId);

    /**
     * 根据管理者id查询企业
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/company/manager/{id}")
    @ApiOperation(value = "根据企业管理ID查找企业")
    public Result<Company> findByManagerId(@PathVariable(value = "id") String id);

    /**
     * 获取某个企业的部门列表
     *
     * @return
     */
    @GetMapping(value = "company/department")
    @ApiOperation(value = "获取某个企业的部门列表")
    public Result<DeptListResult> findAll();
}
