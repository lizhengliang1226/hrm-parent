package com.hrm.company.service.impl;

import com.hrm.common.service.BaseSpecService;
import com.hrm.company.dao.DepartmentDao;
import com.hrm.company.service.DepartmentService;
import com.hrm.domain.company.Department;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }


    @Override
    public void save(Department department) {
        String id = IdWorker.getIdStr();
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
        return departmentDao.findAll(getSameCompanySpec(id));
    }

    @Override
    public void deleteById(String id) {
        final List<Department> all = departmentDao.findAll(getSameDepartmentSpec(id));
        all.forEach(dept -> {
            departmentDao.deleteById(dept.getId());
        });
        departmentDao.deleteById(id);
    }

    @Override
    public Department findByCode(String code, String companyId) {
        return departmentDao.findByCodeAndCompanyId(code, companyId);
    }
}
