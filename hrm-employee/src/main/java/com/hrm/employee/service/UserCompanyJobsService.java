package com.hrm.employee.service;

import com.hrm.domain.employee.UserCompanyJobs;

/**
 * 员工岗位信息服务
 *
 * @author LZL
 * @date 2022/3/15-0:55
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
