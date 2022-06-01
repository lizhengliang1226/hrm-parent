package com.hrm.attendance.dao;


import com.hrm.domain.attendance.entity.LeaveConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LeaveConfigDao extends CrudRepository<LeaveConfig, Long>, JpaRepository<LeaveConfig, Long>, JpaSpecificationExecutor<LeaveConfig> {


    /**
     * 根据公司和部门查询请假配置信息
     *
     * @param companyId
     * @param departmentId
     * @return
     */
    List<LeaveConfig> findByCompanyIdAndDepartmentId(String companyId, String departmentId);


    /**
     * 查询请假配置
     * @param companyId
     * @param departmentId
     * @param leaveType
     * @return
     */
    LeaveConfig findByCompanyIdAndDepartmentIdAndLeaveType(String companyId, String departmentId, String leaveType);

}
