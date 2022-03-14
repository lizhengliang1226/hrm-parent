package com.hrm.system.dao;


import com.hrm.domain.system.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 权限数据访问接口
 *
 * @author 17314
 */
public interface PermissionDao extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {
    /**
     * 通过类型和父id查找子权限api
     *
     * @param type
     * @param pid
     * @return
     */
    List<Permission> findByTypeAndPid(Integer type, String pid);
}