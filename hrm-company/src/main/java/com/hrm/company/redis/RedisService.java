package com.hrm.company.redis;

import com.hrm.company.dao.DepartmentDao;
import com.hrm.domain.company.Department;
import com.hrm.domain.constant.SystemConstant;
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
    private DepartmentDao departmentDao;

    public void buildDeptData(String companyId) {
        final List<Department> all = departmentDao.findByCompanyId(companyId);
        for (Department department : all) {
            final String id = department.getId();
            final String code = department.getCode();
            redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).put(id, department);
            redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).put(code, department);
        }
    }

    public Department getDeptByCode(String code) {
        final Object o = redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).get(code);
        return (Department) o;
    }

}
