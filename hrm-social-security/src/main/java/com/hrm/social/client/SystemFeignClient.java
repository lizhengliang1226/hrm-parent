package com.hrm.social.client;

import com.hrm.common.entity.Result;
import com.hrm.domain.system.City;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 系统微服务远程调用
 */
@FeignClient("hrm-system")
public interface SystemFeignClient {
    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sys/user/{id}", method = RequestMethod.GET)
    Result findById(@PathVariable(value = "id") String id);

    @GetMapping("sys/city")
    public Result<List<City>> findAll();
}
