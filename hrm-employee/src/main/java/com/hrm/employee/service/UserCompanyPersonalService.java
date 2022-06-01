package com.hrm.employee.service;

import com.hrm.domain.employee.UserCompanyPersonal;
import org.springframework.data.domain.Page;

import java.util.Map;

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
    Page<Map> findMonthlyReport(String companyId, String month, Integer page, Integer pageSize);

    /**
     * 查询企业某月的在职或离职人数
     *
     * @param companyId
     * @param month
     * @return
     */
    Integer numOfJobStatus(String companyId, String month, int status);
}
