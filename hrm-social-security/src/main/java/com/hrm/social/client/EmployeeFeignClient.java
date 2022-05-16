package com.hrm.social.client;


import com.hrm.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 员工微服务调用
 */
@FeignClient("hrm-employee")
public interface EmployeeFeignClient {

    /**
     * 获取个人信息
     *
     * @param uid 用户id
     * @return 个人信息
     */
    @GetMapping(value = "employees/{id}/personalInfo")
    Result findPersonalInfo(@PathVariable(name = "id") String uid);
}
