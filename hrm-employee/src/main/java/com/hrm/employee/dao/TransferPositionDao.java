package com.hrm.employee.dao;


import com.hrm.domain.employee.EmployeeTransferPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 调岗信息数据接口
 *
 * @author 17314
 */
public interface TransferPositionDao extends JpaRepository<EmployeeTransferPosition, String>, JpaSpecificationExecutor<EmployeeTransferPosition> {
    /**
     * 查询调岗信息
     *
     * @param uid 用户id
     * @return 调岗信息
     */
    EmployeeTransferPosition findByUserId(String uid);
}