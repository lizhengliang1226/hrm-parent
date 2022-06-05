package com.hrm.salary.controller;


import com.hrm.common.client.SystemFeignClient;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.salary.UserSalary;
import com.hrm.domain.salary.vo.SalaryItemVo;
import com.hrm.salary.service.SalaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("salary")
@Api(tags = "薪资设置")
@Slf4j
public class SalaryController extends BaseController {
    @Autowired
    private SystemFeignClient systemFeignClient;
    @Autowired
    private SalaryService salaryService;

    @ApiOperation(value = "查询某个用户薪资")
    @GetMapping(value = "/modify/{userId}")
    public Result findUserSalary(@PathVariable(value = "userId") String userId) throws Exception {
        UserSalary userSalary = salaryService.findUserSalary(userId);
        return new Result(ResultCode.SUCCESS, userSalary);
    }

    @ApiOperation(value = "调薪")
    @PostMapping(value = "/modify/{userId}")
    public Result modify(@RequestBody UserSalary userSalary) throws Exception {
        salaryService.saveUserSalary(userSalary);
        return new Result(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "定薪")
    @PostMapping(value = "/init")
    public Result init(@RequestBody UserSalary userSalary) {
        userSalary.setFixedBasicSalary(userSalary.getCurrentBasicSalary());
        userSalary.setFixedPostWage(userSalary.getCurrentPostWage());
        salaryService.saveUserSalary(userSalary);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping(value = "/list")
    public Result list(@RequestBody Map map) {
        map.put("companyId", companyId);
        PageResult<SalaryItemVo> pr = salaryService.findAll(map);
        return new Result(ResultCode.SUCCESS, pr);
    }

	@GetMapping(value = "tips/{yearMonth}")
	@ApiOperation(value = "获取企业薪资月度基础信息")
	public Result findBaseInfo(@PathVariable(value = "yearMonth") String yearMonth) {
		//todo
		// {{tips.dateRange}}：入职 {{tips.worksCount}} 离职 {{tips.leavesCount}} 调薪 {{tips.adjustCount}}
		// 未定薪 {{tips.unGradingCount}}
		Map map = new HashMap(8);
		map.put("worksCount", 190);
		map.put("adjustCount", 190);
		map.put("leavesCount", 33);
		map.put("unGradingCount", 33);
		log.info(yearMonth);
		log.info("{}", map);
		return new Result(ResultCode.SUCCESS, map);
	}

	@GetMapping("{userId}")
	@ApiOperation(value = "获取用户薪资详情")
	public Result findUserSalaryInfo(String yearMonth, String userId) {
		final Map userSalaryDetail = salaryService.findUserSalaryDetail(userId, yearMonth);
		return new Result(ResultCode.SUCCESS, userSalaryDetail);
	}
}
