package com.hrm.attendance.dao;


import com.hrm.domain.attendance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    public User findByMobile(String mobile);

    List<User> findByCompanyId(String companyId);

    /**
     * 查询某个企业某个部门的人数
     *
     * @param companyId
     * @param departmentId
     * @return
     */
    Integer countByCompanyIdAndDepartmentId(String companyId, String departmentId);

    /**
     * 根据企业id和部门id查询某个部门的用户
     *
     * @param companyId
     * @param departmentId
     * @return
     */
    List<User> findByCompanyIdAndDepartmentId(String companyId, String departmentId);
}
