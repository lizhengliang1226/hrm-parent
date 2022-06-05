package com.hrm.system.controller;


import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.exception.CommonException;
import com.hrm.domain.system.City;
import com.hrm.system.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 城市微服务
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/sys/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping
    public Result save(@RequestBody City city) {
        //业务操作
        cityService.add(city);
        return new Result(ResultCode.SUCCESS);
    }

    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(value = "id") String id, @RequestBody City city) {
        //业务操作
        city.setId(id);
        cityService.update(city);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable(value = "id") String id) {
        cityService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable(value = "id") String id) throws CommonException {
        City city = cityService.findById(id);
        return new Result(ResultCode.SUCCESS, city);
    }

    @GetMapping
    public Result<List<City>> findCityList() {
        List<City> list = cityService.findAll();
        Result<List<City>> result = new Result(ResultCode.SUCCESS);
        result.setData(list);
        return result;
    }
}
