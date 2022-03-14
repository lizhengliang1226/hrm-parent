package com.hrm.company.service;

import com.hrm.domain.company.Department;

import java.util.List;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/7-19:18
 */
public interface DepartmentService {
    /**
     * 保存部门
     *
     * @param department
     */
    public void save(Department department);

    /**
     * 更新部门
     *
     * @param department
     */
    public void update(Department department);

    /**
     * 根据id查询部门
     *
     * @param id
     * @return
     */
    public Department findById(String id);

    /**
     * 查询全部部门列表
     */
    public List<Department> findAll(String id);

    /**
     * 根据id删除部门
     *
     * @param id
     */
    public void deleteById(String id);
}
