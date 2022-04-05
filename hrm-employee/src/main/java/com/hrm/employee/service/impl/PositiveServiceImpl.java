package com.hrm.employee.service.impl;


import com.hrm.common.service.BaseServiceImpl;
import com.hrm.domain.employee.EmployeePositive;
import com.hrm.domain.employee.UserCompanyJobs;
import com.hrm.employee.dao.PositiveDao;
import com.hrm.employee.dao.UserCompanyJobsDao;
import com.hrm.employee.service.PositiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 17314
 */
@Service
public class PositiveServiceImpl extends BaseServiceImpl<PositiveDao, EmployeePositive, String> implements PositiveService {
    private PositiveDao positiveDao;
    private UserCompanyJobsDao userCompanyJobsDao;

    @Autowired
    public void setPositiveDao(PositiveDao positiveDao) {
        this.positiveDao = positiveDao;
    }

    @Autowired
    public void setUserCompanyJobsDao(UserCompanyJobsDao userCompanyJobsDao) {
        this.userCompanyJobsDao = userCompanyJobsDao;
    }

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
    public void save(EmployeePositive positive) {
        positive.setCreateTime(new Date());
        // 已执行
        positive.setEstatus(2);
        final UserCompanyJobs byUserId = userCompanyJobsDao.findByUserId(positive.getUserId());
        byUserId.setCorrectionEvaluation(positive.getCorrectionEvaluation());
        byUserId.setStateOfCorrection("已转正");
        userCompanyJobsDao.save(byUserId);
        positiveDao.save(positive);
    }
}
