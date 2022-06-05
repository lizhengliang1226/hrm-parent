package com.hrm.common.client;

import com.hrm.common.entity.Result;
import com.hrm.domain.salary.UserSalary;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 薪资微服务远程调用
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/19-17:12
 */
@FeignClient(name = "hrm-salary")
public interface SalaryFeignClient {
    @ApiOperation(value = "定薪")
    @PostMapping(value = "salary/init")
    public Result init(@RequestBody UserSalary userSalary);

    @ApiOperation(value = "查询某个用户薪资")
    @GetMapping(value = "salary/modify/{userId}")
    public Result findUserSalary(@PathVariable(value = "userId") String userId) throws Exception;
}
