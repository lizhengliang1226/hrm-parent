package com.hrm.system.dao;

import com.hrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/8-20:31
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    /**
     * 查找用户通过手机
     *
     * @param mobile 手机号
     * @return 用户实体
     */
    User findByMobile(String mobile);

    /**
     * 更新密码
     *
     * @param id       用户id
     * @param password 密码
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "update User set password=?2 where id=?1")
    void updatePassword(String id, String password);
}
