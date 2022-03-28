package com.hrm.employee.service.impl;


import com.hrm.domain.employee.UserCompanyPersonal;
import com.hrm.employee.dao.UserCompanyPersonalDao;
import com.hrm.employee.service.UserCompanyPersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 17314
 */
@Service
public class UserCompanyPersonalServiceImpl implements UserCompanyPersonalService {

    private UserCompanyPersonalDao userCompanyPersonalDao;

    @Autowired
    public void setUserCompanyPersonalDao(UserCompanyPersonalDao userCompanyPersonalDao) {
        this.userCompanyPersonalDao = userCompanyPersonalDao;
    }

    @Override
    public void save(UserCompanyPersonal personalInfo) {
        userCompanyPersonalDao.save(personalInfo);
    }

    @Override
    public UserCompanyPersonal findById(String userId) {
        return userCompanyPersonalDao.findByUserId(userId);
    }
}
