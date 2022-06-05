package com.hrm.system.controller;

import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 *
 * @author LZL
 * @version v1.0
 * @date 2022/6/2-23:00
 */
@Slf4j
@RestController
@RequestMapping("sys")
@Api(tags = "首页信息")
public class DashboardController extends BaseController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "dashboard/info")
    @ApiOperation(value = "查询首页显示信息")
    public Result findInfo(@RequestParam String yearMonth) {
        final int inJob = userService.findInJobUsers(companyId);
        final int entry = userService.findByTimeOfEntry(yearMonth + "%", companyId).size();
        Map<String, Object> map = new HashMap() {{
            put("inJob", inJob);
            put("entry", entry);
        }};
        return new Result(ResultCode.SUCCESS, map);
    }

}
