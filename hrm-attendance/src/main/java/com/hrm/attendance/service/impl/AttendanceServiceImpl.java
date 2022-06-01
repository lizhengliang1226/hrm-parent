package com.hrm.attendance.service.impl;


import cn.hutool.core.date.DatePattern;
import com.hrm.attendance.dao.*;
import com.hrm.attendance.mapper.ArchiveMonthlyInfoMapper;
import com.hrm.attendance.mapper.AttendanceMapper;
import com.hrm.attendance.service.AttendanceService;
import com.hrm.common.entity.PageResult;
import com.hrm.common.utils.DateUtils;
import com.hrm.common.utils.PageUtils;
import com.hrm.domain.attendance.bo.AtteItemBO;
import com.hrm.domain.attendance.entity.Attendance;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.attendance.entity.AttendanceCompanySettings;
import com.lzl.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 考勤服务
 *
 * @author 17314
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private DeductionDictDao deductionDictDao;
    @Autowired
    private AttendanceCompanySettingsDao attendanceCompanySettingsDao;
    @Autowired
    private AttendanceConfigDao attendanceConfigDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ArchiveMonthlyInfoDao archiveMonthlyInfoDao;
    @Autowired
    private AttendanceMapper attendanceMapper;

    @Override
    public Map getAtteData(Map map1) throws ParseException {
        final String companyId = (String) map1.get("companyId");
        PageUtils.doPage(map1);
        // 查询企业考勤月份设置
        final AttendanceCompanySettings data = attendanceCompanySettingsDao.findById(companyId).get();
        // 获取月份
        final String dataMonth = data.getDataMonth();
        String days = DateUtils.getMonthDays(dataMonth, DatePattern.SIMPLE_MONTH_PATTERN) + "";
        String endTime = dataMonth + days;
        String startTime = dataMonth + "01";
        map1.put("start", startTime);
        map1.put("end", endTime);
        // 查询考勤数据
        final List<AtteItemBO> monthAtteData = attendanceMapper.findMonthAtteData(map1);
        // 查询考勤记录数
        final Integer integer = attendanceMapper.countsOfUserAtte(map1);
        // 构建分页结果
        final PageResult<AtteItemBO> listPageResult = new PageResult<>();
        listPageResult.setTotal(Long.valueOf(integer));
        listPageResult.setRows(monthAtteData);
        // 构建返回结果
        Map<String, Object> map = new HashMap(16);
        // 每个员工考勤记录
        map.put("data", listPageResult);
        // 待办任务数，随便写的一个，这个功能没做
        map.put("tobeTaskCount", 11);
        // 考勤月份05
        map.put("monthOfReport", dataMonth.substring(4));
        // 当月有几天31
        map.put("days", DateUtils.getMonthDays(dataMonth, DatePattern.SIMPLE_MONTH_PATTERN));
        // 考勤年月202205
        map.put("dataMonth", dataMonth);
        return map;
    }


    @Override
    public void saveOrUpdateAtte(Attendance attendance) {
        //1.查询考勤是否存在,更新
        Attendance vo = attendanceDao.findByUserIdAndDay(attendance.getUserId(), attendance.getDay());
        //2.如果不存在,设置对象id,保存
        if (vo == null) {
            attendance.setId(IdWorker.getIdStr());
        } else {
            attendance.setId(vo.getId());
        }
        attendanceDao.save(attendance);
    }


    @Override
    public PageResult<AttendanceArchiveMonthlyInfo> getReports(String atteDate, String companyId, int page, int pagesize) {
        Map map = new HashMap() {{
            put("month", atteDate + "%");
            put("page", (page - 1) * pagesize);
            put("pagesize", pagesize);
            put("companyId", companyId);
        }};
        final List<AttendanceArchiveMonthlyInfo> attendanceArchiveMonthlyInfos1 = archiveMonthlyInfos.userAtteDays(map);
        final long integer = archiveMonthlyInfos.countsOfAtteDatabase(atteDate + "%", companyId);
        return new PageResult<>(
                integer, attendanceArchiveMonthlyInfos1);
    }

    @Autowired
    private ArchiveMonthlyInfoMapper archiveMonthlyInfos;

    @Override
    public void newReport(String yearMonth, String companyId) {
        AttendanceCompanySettings attendanceCompanySettings = attendanceCompanySettingsDao.findById(companyId).get();
        attendanceCompanySettings.setDataMonth(yearMonth);
        attendanceCompanySettingsDao.save(attendanceCompanySettings);
    }

    @Override
    public AttendanceCompanySettings findMonthById(String companyId) {
        final Optional<AttendanceCompanySettings> byId = attendanceCompanySettingsDao.findById(companyId);
        return byId.orElse(null);
    }

    @Override
    public void saveSetMonth(AttendanceCompanySettings companySettings) {
        companySettings.setIsSettings(1);
        attendanceCompanySettingsDao.save(companySettings);
    }
}
