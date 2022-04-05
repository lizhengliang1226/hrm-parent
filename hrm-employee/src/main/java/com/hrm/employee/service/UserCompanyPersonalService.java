package com.hrm.employee.service;

import com.hrm.domain.employee.UserCompanyPersonal;

/**
 * 员工详细信息服务
 *
 * @author LZL
 * @date 2022/3/15-0:52
 */
public interface UserCompanyPersonalService {
    /**
     * 保存员工详细信息
     *
     * @param personalInfo
     */
    public void save(UserCompanyPersonal personalInfo);

    /**
     * 查询员工详细信息通过id
     *
     * @param userId
     * @return
     */
    public UserCompanyPersonal findById(String userId);
}
