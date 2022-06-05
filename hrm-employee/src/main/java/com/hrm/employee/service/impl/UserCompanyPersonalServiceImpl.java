package com.hrm.employee.service.impl;


import com.hrm.domain.employee.UserCompanyPersonal;
import com.hrm.employee.dao.UserCompanyPersonalDao;
import com.hrm.employee.service.UserCompanyPersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 17314
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserCompanyPersonalServiceImpl implements UserCompanyPersonalService {

    private UserCompanyPersonalDao userCompanyPersonalDao;
    private ReentrantLock lock = new ReentrantLock();
    @Autowired
    public void setUserCompanyPersonalDao(UserCompanyPersonalDao userCompanyPersonalDao) {
        this.userCompanyPersonalDao = userCompanyPersonalDao;
    }

    @Override
    public void save(UserCompanyPersonal personalInfo) {
        lock.lock();
        try {
            userCompanyPersonalDao.save(personalInfo);
        } finally {
            lock.unlock();
        }

    }

    @Override
    public UserCompanyPersonal findById(String userId) {
        return userCompanyPersonalDao.findByUserId(userId);
    }

    @Override
    public Page<Map> findMonthlyReport(String companyId, String month, Integer page, Integer pageSize) {
        Page<Map> list = null;
        if (page == null || pageSize == null) {
            list = userCompanyPersonalDao.findByTimeOfEntryAndResignationTime(companyId, month + "%", null);
        } else {
            list = userCompanyPersonalDao.findByTimeOfEntryAndResignationTime(companyId, month + "%", PageRequest.of(page - 1, pageSize));
        }
        return list;
    }

    @Override
    public Integer numOfJobStatus(String companyId, String month, int status) {
        return userCompanyPersonalDao.numOfJobStatus(companyId, month + "%", status);
    }


}
