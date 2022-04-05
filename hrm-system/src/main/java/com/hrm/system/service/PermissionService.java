package com.hrm.system.service;

import com.hrm.common.exception.CommonException;
import com.hrm.domain.system.Permission;

import java.util.List;
import java.util.Map;

/**
 * 权限服务
 *
 * @author LZL
 * @date 2022/3/10-6:07
 */
public interface PermissionService {
    /**
     * 保存权限
     * @param map 权限信息
     * @throws Exception 异常
     */
    void save(Map<String, Object> map) throws Exception;

    /**
     * 更新权限
     *@throws Exception 异常
     * @param map 权限信息
     */
    void update(Map<String, Object> map) throws Exception;

    /**
     * 查找权限
     *
     * @param id 权限id
     * @return 权限信息
     * @throws CommonException 异常
     */
    Map<String, Object> findById(String id) throws Exception;

    /**
     * 查找权限列表
     *
     * @param map 条件列表
     * @return 权限
     */
    List<Permission> findAll(Map<String, Object> map);

    /**
     * 删除权限
     *
     * @param id 权限id
     * @throws CommonException 异常
     */
    void deleteById(String id) throws Exception;
}
