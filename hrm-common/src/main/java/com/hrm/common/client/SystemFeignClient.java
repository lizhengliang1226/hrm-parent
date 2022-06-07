package com.hrm.common.client;

import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.domain.system.City;
import com.hrm.domain.system.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取城市列表
     *
     * @return
     */
    @GetMapping("sys/city")
    public Result<List<City>> findCityList();

    @GetMapping(value = "sys/user", name = "FIND_USER_LIST_API")
    @ApiOperation(value = "获取某个企业的用户列表")
    public Result<PageResult<User>> findAll(@RequestParam Map<String, Object> map);

    @GetMapping(value = "sys/findUsers")
    @ApiOperation(value = "获取某个企业的用户列表,不获取角色")
    public Result<Long> findAllUsers(@RequestParam Map<String, Object> map);
}
