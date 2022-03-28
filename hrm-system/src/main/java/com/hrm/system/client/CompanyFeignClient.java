package com.hrm.system.client;

import com.hrm.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description 调用企业微服务客户端
 * @Author LZL
 * @Date 2022/3/15-0:18
 */
@FeignClient("hrm-company")
public interface CompanyFeignClient {
    /**
     * 调用企业微服务根据id查找部门信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "department/{id}", name = "FIND_DEPT_API")
    public Result findById(@PathVariable(value = "id") String id);
}
