package com.hrm.employee.service.impl;

import com.hrm.domain.employee.UserCompanyJobs;
import com.hrm.employee.dao.UserCompanyJobsDao;
import com.hrm.employee.service.UserCompanyJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 17314
 */
@Service
public class UserCompanyJobsServiceImpl implements UserCompanyJobsService {
    @Autowired
    private UserCompanyJobsDao userCompanyJobsDao;

    @Override
    public void save(UserCompanyJobs jobsInfo) {
        userCompanyJobsDao.save(jobsInfo);
    }

    @Override
    public UserCompanyJobs findById(String userId) {
        return userCompanyJobsDao.findByUserId(userId);
    }
}
