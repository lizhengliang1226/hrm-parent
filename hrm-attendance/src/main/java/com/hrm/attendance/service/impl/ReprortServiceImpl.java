package com.hrm.attendance.service.impl;


import com.hrm.attendance.dao.ArchiveMonthlyDao;
import com.hrm.attendance.service.ReprortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReprortServiceImpl implements ReprortService {


    @Autowired
    private ArchiveMonthlyDao archiveMonthlyDao;

}
