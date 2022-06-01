package com.hrm.company.service.impl;

import com.hrm.common.service.BaseSpecService;
import com.hrm.company.dao.DepartmentDao;
import com.hrm.company.service.DepartmentService;
import com.hrm.domain.company.Department;
import com.hrm.domain.constant.SystemConstant;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author LZL
 * @date 2022/3/7-19:18
 */
@Service
public class DepartmentServiceImpl extends BaseSpecService<Department> implements DepartmentService {

    private DepartmentDao departmentDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }


    @Override
    public void save(Department department) {
        String id = IdWorker.getIdStr();
        department.setId(id);
        department.setCreateTime(new Date());
        departmentDao.save(department);
        redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).put(department.getId(), department);
        redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).put(department.getCode(), department);
    }

    @Override
    public void update(Department department) {
        Department dept = departmentDao.findById(department.getId()).get();
        dept.setCode(department.getCode());
        dept.setName(department.getName());
        dept.setIntroduce(department.getIntroduce());
        dept.setCompanyId(department.getCompanyId());
        dept.setPid(department.getPid());
        dept.setManager(department.getManager());
        departmentDao.save(dept);
        redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).put(department.getId(), department);
        redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).put(department.getCode(), department);
    }

    @Override
    public Department findById(String id) {
        return departmentDao.findById(id).get();
    }

    @Override
    public List<Department> findAll(String id) {
        return departmentDao.findAll(getSameCompanySpec(id));
    }

    @Override
    public void deleteById(String id) {
        final List<Department> all = departmentDao.findAll(getSameDepartmentSpec(id));
        final Department department = departmentDao.findById(id).get();
        // 级联删除子部门
        all.forEach(dept -> {
            departmentDao.deleteById(dept.getId());
            redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).delete(dept.getId());
            redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).delete(dept.getCode());
        });
        departmentDao.deleteById(id);
        redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).delete(id);
        redisTemplate.boundHashOps(SystemConstant.REDIS_DEPT_LIST).delete(department.getCode());
    }

    @Override
    public Department findByCode(String code, String companyId) {
        return departmentDao.findByCodeAndCompanyId(code, companyId);
    }
}
