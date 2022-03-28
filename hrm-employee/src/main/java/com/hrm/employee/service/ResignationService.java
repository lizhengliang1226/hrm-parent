package com.hrm.employee.service;

import com.hrm.domain.employee.EmployeeResignation;

/**
 * @Description 员工离职申请服务
 * @Author LZL
 * @Date 2022/3/15-0:57
 */
public interface ResignationService {
    /**
     * 离职信息保存
     *
     * @param resignation
     */
    public void save(EmployeeResignation resignation);

    /**
     * 离职信息查询
     *
     * @param userId
     * @return
     */
    public EmployeeResignation findById(String userId);
}
