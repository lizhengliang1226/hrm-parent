package com.hrm.system.dao;

import com.hrm.common.dao.BaseDao;
import com.hrm.domain.system.Role;

/**
 * 角色数据访问接口
 *
 * @author LZL
 * @date 2022/3/9-1:03
 */
public interface RoleDao extends BaseDao<Role, String> {
    /**
     * 查询角色信息通过name
     *
     * @param name 角色名
     * @return 角色信息
     */
    Role findByName(String name);
}
