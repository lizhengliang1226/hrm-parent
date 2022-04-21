package com.hrm.system.service;

import com.hrm.common.exception.CommonException;
import com.hrm.common.service.BaseService;
import com.hrm.domain.system.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 角色服务
 *
 * @author LZL
 * @date 2022/3/9-1:07
 */
public interface RoleService extends BaseService<Role, String> {
    /**
     * 保存角色
     *
     * @param role 角色
     */
    public void save(Role role) throws CommonException;

    /**
     * 更新角色
     *
     * @param role 角色
     */
    public void update(Role role);

    /**
     * 分页查找角色列表
     *
     * @param map 查询条件的集合
     * @return 角色列表
     */
    public Page<Role> findSearch(Map<String, Object> map);

    /**
     * 查找角色列表
     *
     * @param map 查询条件的集合
     * @return 角色列表
     */
    public List<Role> findAll(Map<String, Object> map);

    /**
     * 为角色分配权限
     *
     * @param id          角色id
     * @param permissions 权限列表
     */
    void assignPerms(String id, List<String> permissions);
}
