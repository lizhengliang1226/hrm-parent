package com.hrm.system.dao;

import com.hrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/8-20:31
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    /**
     * 查找用户通过手机
     *
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);
}
