package com.hrm.employee.service;

import com.hrm.domain.employee.EmployeePositive;

/**
 * @Description 转正服务
 * @Author LZL
 * @Date 2022/3/15-0:57
 */
public interface PositiveService {
    /**
     * 查询转正信息
     *
     * @param uid
     * @param status
     * @return
     */
    public EmployeePositive findById(String uid, Integer status);

    /**
     * 查询转正信息通过id
     *
     * @param uid
     * @return
     */
    public EmployeePositive findById(String uid);

    /**
     * 保存转正信息
     *
     * @param positive
     */
    public void save(EmployeePositive positive);
}
