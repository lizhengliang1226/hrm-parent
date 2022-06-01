package com.hrm.salary.controller;


import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.salary.SalaryCompanySettings;
import com.hrm.salary.service.SalaryCompanySettingsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("salary")
@Api(tags = "薪资月份设置")
@Slf4j
public class CompanySettingsController extends BaseController {

	@Autowired
	private SalaryCompanySettingsService companySettingsService;

	/**
	 * 获取企业是否设置工资
	 */
	@RequestMapping(value = "/company-settings", method = RequestMethod.GET)
	public Result getCompanySettings() throws Exception {
		SalaryCompanySettings companySettings = companySettingsService.findById(companyId);
		return new Result(ResultCode.SUCCESS, companySettings);
	}

    /**
     * 保存企业工资设置
     */
    @RequestMapping(value = "/company-settings", method = RequestMethod.POST)
    public Result saveCompanySettings(@RequestBody SalaryCompanySettings companySettings) throws Exception {
        companySettings.setCompanyId(companyId);
        companySettingsService.save(companySettings);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 新建报表
     *
     * @param yearMonth
     * @return
     */
    @GetMapping(value = "/reports/newReport")
    public Result newReport(String yearMonth) {
        SalaryCompanySettings companySettings = new SalaryCompanySettings();
        companySettings.setCompanyId(companyId);
        companySettings.setDataMonth(yearMonth);
        companySettingsService.save(companySettings);
        return new Result(ResultCode.SUCCESS);
    }
}
