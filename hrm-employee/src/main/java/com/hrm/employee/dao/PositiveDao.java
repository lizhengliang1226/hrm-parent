package com.hrm.employee.dao;

import com.hrm.common.dao.BaseDao;
import com.hrm.domain.employee.EmployeePositive;

/**
 * 数据访问接口
 *
 * @author 17314
 */
public interface PositiveDao extends BaseDao<EmployeePositive, String> {
    /**
     * 查询转正信息
     *
     * @param uid 用户id
     * @return 转正信息
     */
    EmployeePositive findByUserId(String uid);
}