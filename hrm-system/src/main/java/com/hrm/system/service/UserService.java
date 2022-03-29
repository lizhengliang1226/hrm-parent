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
     * @param user 用户
     */
    void save(User user);

    /**
     * 更新用户
     *
     * @param user 用户
     */
    void update(User user);

    /**
     * 查找用户
     *
     * @param id 用户id
     * @return 用户
     */
    User findById(String id);

    /**
     * 查找用户通过手机
     *
     * @param mobile 手机号
     * @return 用户
     */
    User findByMobile(String mobile);

    /**
     * 分页查找用户列表
     *
     * @param map 条件
     * @return 用户列表
     */
    Page<User> findAll(Map<String, Object> map);

    /**
     * 查找全部用户列表
     *
     * @param companyId 企业id
     * @return 用户列表
     */
    List<User> findSimpleUsers(String companyId);

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    void deleteById(String id);

    /**
     * 给用户分配角色
     *
     * @param id    用户id
     * @param roles 角色数组
     */
    void assignRoles(String id, List<String> roles);

    /**
     * 更新密码
     *
     * @param id       用户id
     * @param password 密码
     */
    void updatePassword(String id, String password);
}
