package com.hrm.company.service.impl;

import com.hrm.common.service.BaseService;
import com.hrm.common.utils.IdWorker;
import com.hrm.company.dao.DepartmentDao;
import com.hrm.company.service.DepartmentService;
import com.hrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/7-19:18
 */
@Service
public class DepartmentServiceImpl extends BaseService<Department> implements DepartmentService {

    private IdWorker idWorker;

    private DepartmentDao departmentDao;

    @Autowired
    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Autowired
    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }

    @Override
    public void save(Department department) {
        String id = idWorker.nextId() + "";
        department.setId(id);
        department.setCreateTime(new Date());
        departmentDao.save(department);
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
    }

    @Override
    public Department findById(String id) {
        return departmentDao.findById(id).get();
    }

    @Override
    public List<Department> findAll(String id) {
        return departmentDao.findAll(getSpec(id));
    }

    @Override
    public void deleteById(String id) {
        final List<Department> all = departmentDao.findAll(getAllChild(id));
        all.forEach(dept -> {
            departmentDao.deleteById(dept.getId());
        });
        departmentDao.deleteById(id);
    }
}
