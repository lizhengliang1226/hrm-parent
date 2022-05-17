package com.hrm.attendance.service.impl;

import cn.hutool.core.date.DatePattern;
import com.hrm.attendance.dao.ArchiveMonthlyDao;
import com.hrm.attendance.dao.ArchiveMonthlyInfoDao;
import com.hrm.attendance.dao.AttendanceDao;
import com.hrm.attendance.dao.UserDao;
import com.hrm.attendance.service.ArchiveService;
import com.hrm.common.utils.DateUtils;
import com.hrm.domain.attendance.entity.ArchiveMonthly;
import com.hrm.domain.attendance.entity.ArchiveMonthlyInfo;
import com.hrm.domain.attendance.entity.User;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 归档服务实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/16-17:29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private ArchiveMonthlyDao atteArchiveMonthlyDao;

    @Autowired
    private ArchiveMonthlyInfoDao archiveMonthlyInfoDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void saveArchive(String archiveDate, String companyId) {
        //1.查询所有企业用户
        List<User> users = userDao.findByCompanyId(companyId);
        //1.保存归档主表数据
        ArchiveMonthly archiveMonthly = new ArchiveMonthly();
        archiveMonthly.setId(IdWorker.getIdStr());
        archiveMonthly.setCompanyId(companyId);
        archiveMonthly.setArchiveYear(archiveDate.substring(0, 4));
        archiveMonthly.setArchiveMonth(archiveDate.substring(5));
        //2.保存归档明细表数据
        for (User user : users) {
            ArchiveMonthlyInfo info = new ArchiveMonthlyInfo(user);
            //统计每个用户的考勤记录
            Map map = attendanceDao.statisticalByUser(user.getId(), archiveDate + "%");
            info.setStatisData(map);
            info.setId(IdWorker.getIdStr());
            info.setAtteArchiveMonthlyId(archiveMonthly.getId());
            archiveMonthlyInfoDao.save(info);
        }
        //总人数
        archiveMonthly.setTotalPeopleNum(users.size());
        // 全勤人数
        final int monthDays = DateUtils.getMonthDays(archiveDate, DatePattern.SIMPLE_MONTH_PATTERN);
        final List<Integer> fullAtteNumber = attendanceDao.getFullAtteNumber(companyId, archiveDate + "%");
        Integer fullAtteUsers = 0;
        for (Integer integer : fullAtteNumber) {
            if (integer == monthDays) {
                fullAtteUsers += 1;
            }
        }
        archiveMonthly.setFullAttePeopleNum(fullAtteUsers);
        archiveMonthly.setIsArchived(0);
        atteArchiveMonthlyDao.save(archiveMonthly);
    }
}
