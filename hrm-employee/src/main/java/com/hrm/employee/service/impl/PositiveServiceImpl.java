package com.hrm.employee.service.impl;


import com.hrm.domain.employee.EmployeePositive;
import com.hrm.employee.dao.PositiveDao;
import com.hrm.employee.service.PositiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PositiveServiceImpl implements PositiveService {
    @Autowired
    private PositiveDao positiveDao;

    @Override
    public EmployeePositive findById(String uid, Integer status) {
        EmployeePositive positive = positiveDao.findByUserId(uid);
        if (status != null && positive != null) {
            if (!positive.getEstatus().equals(status)) {
                positive = null;
            }
        }
        return positive;
    }

    @Override
    public EmployeePositive findById(String uid) {
        return positiveDao.findByUserId(uid);
    }

    @Override
    public void save(EmployeePositive positive) {
        positive.setCreateTime(new Date());
        positive.setEstatus(1);//未执行
        positiveDao.save(positive);
    }
}
