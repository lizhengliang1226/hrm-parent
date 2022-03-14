package com.hrm.system.service;

import com.hrm.common.exception.CommonException;
import com.hrm.domain.system.Permission;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/10-6:07
 */
public interface PermissionService {
    /**
     * 保存权限
     * @param map
     * @throws Exception
     */
    void save(Map<String, Object> map) throws Exception;

    /**
     * 更新权限
     *@throws Exception
     * @param map
     */
    void update(Map<String, Object> map) throws Exception;

    /**
     * 查找权限
     *@throws CommonException
     * @param id
     * @return
     */
    Map findById(String id) throws CommonException;

    /**
     * 查找权限列表
     *
     * @param map
     * @return
     */
    List<Permission> findAll(Map<String, Object> map);

    /**
     * 删除权限
     *
     * @param id
     * @throws CommonException
     */
    void deleteById(String id) throws CommonException;
}
