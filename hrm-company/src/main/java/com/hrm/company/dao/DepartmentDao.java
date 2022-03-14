package com.hrm.company.dao;

import com.hrm.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description 部门dao接口
 * @Author LZL
 * @Date 2022/3/7-19:17
 */
public interface DepartmentDao extends JpaRepository<Department, String>, JpaSpecificationExecutor<Department> {
}
