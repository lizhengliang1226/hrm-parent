package com.hrm.employee.dao;

import com.hrm.domain.employee.UserCompanyJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据访问接口
 *
 * @author 17314
 */
public interface UserCompanyJobsDao extends JpaRepository<UserCompanyJobs, String>, JpaSpecificationExecutor<UserCompanyJobs> {
    /**
     * 查询岗位信息
     *
     * @param userId 用户id
     * @return 岗位信息
     */
    UserCompanyJobs findByUserId(String userId);
}