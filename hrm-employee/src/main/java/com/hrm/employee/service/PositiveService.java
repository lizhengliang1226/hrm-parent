package com.hrm.employee.service;

import com.hrm.common.service.BaseService;
import com.hrm.domain.employee.EmployeePositive;

/**
 * 转正服务
 *
 * @author LZL
 * @date 2022/3/15-0:57
 */
public interface PositiveService extends BaseService<EmployeePositive, String> {
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
//    public EmployeePositive findById(String uid);

    /**
     * 保存转正信息
     *
     * @param positive
     */
    public void save(EmployeePositive positive);
}
