package com.hrm.employee.service;

import com.hrm.domain.employee.EmployeeTransferPosition;

/**
 * 员工调岗申请服务
 *
 * @author LZL
 * @date 2022/3/15-0:57
 */
public interface TransferPositionService {
    /**
     * 查询员工调岗信息
     *
     * @param uid
     * @param status
     * @return
     */
    public EmployeeTransferPosition findById(String uid, Integer status);

    /**
     * 保存员工调岗信息
     *
     * @param transferPosition
     */
    public void save(EmployeeTransferPosition transferPosition);
}
