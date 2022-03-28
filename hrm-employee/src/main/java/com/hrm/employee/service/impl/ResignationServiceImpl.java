package com.hrm.employee.service.impl;


import com.hrm.domain.employee.EmployeeResignation;
import com.hrm.employee.dao.EmployeeResignationDao;
import com.hrm.employee.service.ResignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ResignationServiceImpl implements ResignationService {
    @Autowired
    EmployeeResignationDao resignationDao;

    @Override
    public void save(EmployeeResignation resignation) {
        resignation.setCreateTime(new Date());
        resignationDao.save(resignation);
    }

    @Override
    public EmployeeResignation findById(String userId) {
        return resignationDao.findByUserId(userId);
    }
}
