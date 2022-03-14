package com.hrm.common.service;

import org.springframework.data.jpa.domain.Specification;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/7-20:36
 */
public class BaseService<T> {
    /**
     * 查询属于同一家企业的某些信息，比如部门，用户,角色
     *
     * @param companyId
     * @return
     */
    protected Specification<T> getSpec(String companyId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("companyId").as(String.class), companyId);
    }

    /**
     * 在删除时查询当前部门下面的所有子部门，进行级联删除
     *
     * @param deptId
     * @return
     */
    protected Specification<T> getAllChild(String deptId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("pid").as(String.class), deptId);
    }
}
