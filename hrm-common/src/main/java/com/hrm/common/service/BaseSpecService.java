package com.hrm.common.service;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author LZL
 * @date 2022/3/7-20:36
 */
public class BaseSpecService<T> {
    /**
     * 查询条件，企业id相同，即属于同一家企业
     *
     * @param companyId 企业id
     * @return 条件对象
     */
    protected Specification<T> getSameCompanySpec(String companyId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("companyId").as(String.class), companyId);
    }

    /**
     * 查询条件，在删除时查询当前部门下面的所有子部门，进行级联删除
     *
     * @param deptId 部门id
     * @return 条件对象
     */
    protected Specification<T> getSameDepartmentSpec(String deptId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("pid").as(String.class), deptId);
    }

}
