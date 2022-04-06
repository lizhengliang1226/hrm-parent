package com.hrm.employee.service;

import com.hrm.domain.employee.UserCompanyPersonal;
import com.hrm.domain.employee.response.EmployeeReportResult;

import java.util.List;

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

    /**
     * 根据月份和企业id查询当月的员工报表，包括当月离职人员和入职人员
     *
     * @param companyId 企业id
     * @param month     月份
     * @return 员工详细信息列表
     */
    List<EmployeeReportResult> findMonthlyReport(String companyId, String month);
}
