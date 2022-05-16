package com.hrm.system.service;


import com.hrm.domain.system.City;

import java.util.List;


public interface CityService {


    /**
     * 保存城市
     *
     * @param city
     */
    public void add(City city);

    /**
     * 更新
     */
    public void update(City city);

    /**
     * 删除
     */
    public void deleteById(String id);

    /**
     * 根据id查询
     */
    public City findById(String id);

    /**
     * 查询列表
     */
    public List<City> findAll();
}
