package com.hrm.company.service;

import com.hrm.domain.company.Department;

import java.util.List;

/**
 * @author LZL
 * @date 2022/3/7-19:18
 */
public interface DepartmentService {
    /**
     * 保存部门
     *
     * @param department 部门实体
     */
    public void save(Department department);

    /**
     * 更新部门
     *
     * @param department 部门实体
     */
    public void update(Department department);

    /**
     * 根据id查询部门
     *
     * @param id 部门id
     * @return 部门实体
     */
    public Department findById(String id);

    /**
     * 、
     * 查询全部部门
     *
     * @param id 企业id
     * @return 某个企业的全部部门
     */
    public List<Department> findAll(String id);

    /**
     * 根据id删除部门
     *
     * @param id 部门id
     */
    public void deleteById(String id);

    /**
     * 查询部门信息通过部门编码
     *
     * @param code      部门编码
     * @param companyId 企业id
     * @return 部门信息
     */
    Department findByCode(String code, String companyId);
}
