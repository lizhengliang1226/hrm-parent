package com.hrm.employee.dao;

import com.hrm.domain.employee.UserCompanyPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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

}