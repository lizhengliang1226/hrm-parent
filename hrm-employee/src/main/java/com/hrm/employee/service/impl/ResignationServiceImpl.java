package com.hrm.employee.service.impl;


import com.hrm.domain.employee.EmployeeResignation;
import com.hrm.employee.dao.EmployeeResignationDao;
import com.hrm.employee.service.ResignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author 17314
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResignationServiceImpl implements ResignationService {

    EmployeeResignationDao resignationDao;

    @Autowired
    public void setResignationDao(EmployeeResignationDao resignationDao) {
        this.resignationDao = resignationDao;
    }

    @Override
    public void save(EmployeeResignation resignation) {
        resignation.setCreateTime(new Date());
        resignationDao.save(resignation);
    }

    @Override
    public EmployeeResignation findById(String userId) {
        return resignationDao.findByUserId(userId);
    }

    @Override
    public Integer findDepartureNum(String month, String companyId) {
        return resignationDao.countOfDeparture(month + "%", companyId);
    }
}
