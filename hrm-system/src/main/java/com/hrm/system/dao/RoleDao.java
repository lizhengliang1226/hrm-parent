package com.hrm.system.dao;

import com.hrm.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/9-1:03
 */
public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
}
