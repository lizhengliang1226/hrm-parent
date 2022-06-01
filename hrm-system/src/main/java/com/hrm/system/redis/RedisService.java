package com.hrm.system.redis;

import com.alibaba.fastjson.JSON;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.system.User;
import com.hrm.system.dao.UserDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缓存服务
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/21-19:48
 */
@Service
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserDao userDao;

    public void buildUserData(String companyId) {
        final List<User> all = userDao.findByCompanyId(companyId);
        for (User user : all) {
            final com.hrm.domain.attendance.entity.User user1 = new com.hrm.domain.attendance.entity.User();
            BeanUtils.copyProperties(user, user1);
            final String id = user.getId();
            final String mobile = user.getMobile();
            redisTemplate.boundHashOps(SystemConstant.REDIS_USER_LIST).put(id, user1);
            redisTemplate.boundHashOps(SystemConstant.REDIS_USER_LIST).put(mobile, user1);
        }
    }

    public User getUserInfoByRedisTemplate(String key) {
        Object o = redisTemplate.boundValueOps(key).get();
        return JSON.parseObject(JSON.toJSONString(o), User.class);
    }

    public void deleteUser(String id) {
        redisTemplate.delete(id);
    }
}
