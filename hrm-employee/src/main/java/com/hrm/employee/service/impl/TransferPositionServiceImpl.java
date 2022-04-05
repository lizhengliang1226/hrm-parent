package com.hrm.employee.service.impl;


import com.hrm.domain.employee.EmployeeTransferPosition;
import com.hrm.employee.dao.TransferPositionDao;
import com.hrm.employee.service.TransferPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 17314
 */
@Service
public class TransferPositionServiceImpl implements TransferPositionService {
    @Autowired
    private TransferPositionDao transferPositionDao;

    @Override
    public EmployeeTransferPosition findById(String uid, Integer status) {
        EmployeeTransferPosition transferPosition = transferPositionDao.findByUserId(uid);
        if (status != null && transferPosition != null) {
            if (!transferPosition.getEstatus().equals(status)) {
                transferPosition = null;
            }
        }
        return transferPosition;
    }

    @Override
    public void save(EmployeeTransferPosition transferPosition) {
        transferPosition.setCreateTime(new Date());
        //未执行
        transferPosition.setEstatus(1);
        transferPositionDao.save(transferPosition);
    }
}
