package com.hrm.system.service.impl;

import com.hrm.domain.system.City;
import com.hrm.system.dao.CityDao;
import com.hrm.system.service.CityService;
import com.lzl.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 社保缴纳城市服务
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/15-13:03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    /**
     * 保存
     */
    @Override
    public void add(City city) {
        //基本属性的设置
        city.setId(IdWorker.getIdStr());
        cityDao.save(city);
    }


    /**
     * 更新
     */
    @Override
    public void update(City city) {
        cityDao.save(city);
    }

    /**
     * 删除
     */
    @Override
    public void deleteById(String id) {
        cityDao.deleteById(id);
    }

    /**
     * 根据id查询
     */
    @Override
    public City findById(String id) {
        return cityDao.findById(id).get();
    }

    /**
     * 查询列表
     */
    @Override
    public List<City> findAll() {
        return cityDao.findAll();
    }
}
