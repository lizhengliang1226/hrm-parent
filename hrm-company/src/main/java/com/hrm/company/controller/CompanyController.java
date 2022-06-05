package com.hrm.company.controller;

import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.company.service.CompanyService;
import com.hrm.domain.company.Company;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业管理控制器
 *
 * @author LZL
 * @date 2022/1/12-10:44
 */
@CrossOrigin
@RestController
@RequestMapping("company")
@Api(tags = "企业管理")
public class CompanyController {

    private CompanyService companyService;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping(name = "SAVE_COMPANY_API")
    @ApiOperation(value = "保存企业")
    public Result save(@RequestBody Company company) {
        List<Company> company1 = companyService.findByName(company.getName());
        if (company1 != null && company1.size() > 0) {
            return new Result(ResultCode.DUPLICATE_COMPANY_NAME);
        }
        companyService.add(company);
        return Result.SUCCESS();
    }

    @PutMapping(value = "{id}", name = "UPDATE_COMPANY_API")
    @ApiOperation(value = "更新企业")
    public Result update(@PathVariable(value = "id") String id, @RequestBody Company company) {
        final Company byId = companyService.findById(company.getId());
        if (!byId.getName().equals(company.getName())) {
            final List<Company> byName = companyService.findByName(company.getName());
            if (byName != null && byName.size() > 0) {
                return new Result(ResultCode.DUPLICATE_COMPANY_NAME);
            }
        }
        company.setId(id);
        companyService.update(company);
        return Result.SUCCESS();

    }

    @DeleteMapping(value = "{id}", name = "DELETE_COMPANY_API")
    @ApiOperation(value = "根据id删除企业")
    public Result delete(@PathVariable(value = "id") String id) {
        companyService.delete(id);
        return Result.SUCCESS();
    }

    @GetMapping(value = "{id}", name = "FIND_COMPANY_API")
    @ApiOperation(value = "根据ID查找企业")
    public Result findById(@PathVariable(value = "id") String id) {
        final Company company = companyService.findById(id);
        Result<Company> result = Result.SUCCESS();
        result.setData(company);
        return result;
    }

    @GetMapping(value = "manager/{id}")
    @ApiOperation(value = "根据企业管理ID查找企业")
    public Result<Company> findByManagerId(@PathVariable(value = "id") String id) {
        final Company company = companyService.findByManagerId(id);
        Result<Company> result = Result.SUCCESS();
        result.setData(company);
        return result;
    }

    @GetMapping(name = "FIND_COMPANY_LIST_API")
    @ApiOperation(value = "获取企业列表")
    public Result findAll(int page, int size) {
        final PageResult<Company> all = companyService.findAll(page, size);
        return new Result(ResultCode.SUCCESS, all);
    }

}
