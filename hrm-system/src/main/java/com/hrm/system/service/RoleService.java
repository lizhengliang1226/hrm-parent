package com.hrm.system.service;

import com.hrm.domain.system.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/9-1:07
 */
public interface RoleService {
    /**
     * 保存角色
     *
     * @param role
     */
    public void save(Role role);

    /**
     * 更新角色
     *
     * @param role
     */
    public void update(Role role);

    /**
     * 查找角色
     *
     * @param id
     * @return
     */
    public Role findById(String id);

    /**
     * 分页查找角色列表
     *
     * @param map 查询条件的集合
     * @return
     */
    public Page<Role> findSearch(Map<String, Object> map);

    /**
     * 查找角色列表
     *
     * @param map 查询条件的集合
     * @return
     */
    public List<Role> findAll(Map<String, Object> map);

    /**
     * 删除角色
     *
     * @param id
     */
    public void deleteById(String id);

    /**
     * 为角色分配权限
     *
     * @param id
     * @param permissions
     */
    void assignPerms(String id, List<String> permissions);
}
