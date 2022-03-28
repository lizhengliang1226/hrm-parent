package com.hrm.employee.service;

import com.hrm.domain.employee.UserCompanyJobs;

/**
 * @Description 员工岗位信息服务
 * @Author LZL
 * @Date 2022/3/15-0:55
 */
public interface UserCompanyJobsService {
    /**
     * 员工岗位信息保存
     *
     * @param jobsInfo
     */
    public void save(UserCompanyJobs jobsInfo);

    /**
     * 员工岗位信息查找
     *
     * @param userId
     * @return
     */
    public UserCompanyJobs findById(String userId);
}
