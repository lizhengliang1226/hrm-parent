package com.hrm.company.dao;

import com.hrm.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 部门dao接口
 *
 * @author LZL
 * @date 2022/3/7-19:17
 */
public interface DepartmentDao extends JpaRepository<Department, String>, JpaSpecificationExecutor<Department> {
    /**
     * 查询部门信息通过部门编码
     *
     * @param code      部门编码
     * @param companyId 企业id
     * @return 部门实体
     */
    Department findByCodeAndCompanyId(String code, String companyId);
}
