package com.hrm.employee.service;

import com.hrm.domain.employee.EmployeeResignation;

/**
 * 员工离职申请服务
 *
 * @author LZL
 * @date 2022/3/15-0:57
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

    /**
     * 根据月份查询当月离职人数
     *
     * @param month
     * @return
     */
    Integer findDepartureNum(String month, String companyId);
}
