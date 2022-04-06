package com.hrm.employee.dao;

import com.hrm.domain.employee.UserCompanyPersonal;
import com.hrm.domain.employee.response.EmployeeReportResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 *
 * @author 17314
 */
public interface UserCompanyPersonalDao extends JpaRepository<UserCompanyPersonal, String>, JpaSpecificationExecutor<UserCompanyPersonal> {
    /**
     * 查询详细信息
     *
     * @param userId 用户id
     * @return 员工详细信息
     */
    UserCompanyPersonal findByUserId(String userId);

    /**
     * 通过离职时间和入职时间查询当月的月度报表
     *
     * @param companyId 企业id
     * @param month     月份
     * @return 列表
     */
    @Query(value = "select new com.hrm.domain.employee.response.EmployeeReportResult(a,b) " +
            "from UserCompanyPersonal a left join EmployeeResignation b on a.userId=b.userId " +
            "where a.companyId=?1 and a.timeOfEntry like ?2 or b.resignationTime like ?2")
    List<EmployeeReportResult> findByTimeOfEntryAndResignationTime(String companyId, String month);
}