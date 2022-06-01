package com.hrm.attendance.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrm.attendance.mapper.ArchiveMonthlyInfoMapper;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 归档考勤明细信息服务
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/18-15:58
 */
@Service
public class ArchiveMonthlyInfoServiceImpl extends ServiceImpl<ArchiveMonthlyInfoMapper, AttendanceArchiveMonthlyInfo> {
    @Autowired
    ArchiveMonthlyInfoMapper archiveMonthlyInfoMap;

    public void removeByAtteArchiveMonthlyId(String id) {
        //@Delete("delete from atte_archive_monthly_info where atte_archive_monthly_id=#{id}")
        archiveMonthlyInfoMap.removeByAtteArchiveMonthlyId(id);
    }
}
