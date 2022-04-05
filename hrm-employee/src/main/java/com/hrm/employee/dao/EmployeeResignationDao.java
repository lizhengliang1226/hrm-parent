package com.hrm.employee.dao;


import com.hrm.domain.employee.EmployeeResignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据访问接口
 *
 * @author 17314
 */
public interface EmployeeResignationDao extends JpaRepository<EmployeeResignation, String>, JpaSpecificationExecutor<EmployeeResignation> {
    /**
     * 查询离职信息
     *
     * @param uid 用户id
     * @return 离职信息
     */
    EmployeeResignation findByUserId(String uid);
}