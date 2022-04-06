package com.hrm.employee.service.impl;


import com.hrm.domain.employee.UserCompanyPersonal;
import com.hrm.domain.employee.response.EmployeeReportResult;
import com.hrm.employee.dao.UserCompanyPersonalDao;
import com.hrm.employee.service.UserCompanyPersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<EmployeeReportResult> findMonthlyReport(String companyId, String month) {
        final List<EmployeeReportResult> list = userCompanyPersonalDao.findByTimeOfEntryAndResignationTime(companyId, month + "%");
        return list;
    }


}
