package com.hrm.system.service;


import com.hrm.domain.system.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/8-20:33
 */
public interface UserService {
    /**
     * 保存用户
     *
     * @param department
     */
    void save(User department);

    /**
     * 更新用户
     *
     * @param department
     */
    void update(User department);

    /**
     * 查找用户
     *
     * @param id
     * @return
     */
    User findById(String id);

    /**
     * 查找用户通过手机
     *
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 查找用户列表
     *
     * @param map
     * @return
     */
    Page<User> findAll(Map<String, Object> map);

    /**
     * 删除用户
     *
     * @param id
     */
    void deleteById(String id);

    /**
     * 给用户分配角色
     *
     * @param id
     * @param roles
     */
    void assignRoles(String id, List<String> roles);
}
