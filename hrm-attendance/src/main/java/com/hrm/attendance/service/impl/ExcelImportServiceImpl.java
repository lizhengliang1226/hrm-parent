package com.hrm.attendance.service.impl;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.fastjson.JSON;
import com.hrm.attendance.dao.AttendanceConfigDao;
import com.hrm.attendance.dao.AttendanceDao;
import com.hrm.attendance.dao.UserDao;
import com.hrm.attendance.mapper.AttendanceMapper;
import com.hrm.attendance.service.AttendanceMapperService;
import com.hrm.attendance.service.ExcelImportService;
import com.hrm.common.cache.SystemCache;
import com.hrm.common.utils.DateUtils;
import com.hrm.domain.attendance.entity.Attendance;
import com.hrm.domain.attendance.entity.AttendanceConfig;
import com.hrm.domain.attendance.entity.User;
import com.hrm.domain.attendance.enums.AttendanceStatusEnum;
import com.hrm.domain.attendance.vo.AtteUploadVo;
import com.hrm.domain.constant.SystemConstant;
import com.lzl.IdWorker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 17314
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExcelImportServiceImpl implements ExcelImportService {


    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private AttendanceConfigDao attendanceConfigDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Value("${atte.holidays}")
    private String holidays;

    @Value("${atte.workingDays}")
    private String wordingDays;
    @Autowired
    private AttendanceMapperService ams;
    @Autowired
    private RedisTemplate redisTemplate;
    private List<Attendance> atList = new ArrayList<>();
    private List<AttendanceConfig> atcList = new ArrayList<>();

    /**
     * 处理考勤数据的文件上传
     * 参数 :excel文件
     * 企业
     */
    @Override
    public void importAttendanceExcel(MultipartFile file, String companyId) throws Exception {
        // 查询每个部门的出勤配置信息
        final long st = System.currentTimeMillis();
        atcList = attendanceConfigDao.findAll();
        final ExcelReaderBuilder read = EasyExcel.read(file.getInputStream(), AtteUploadVo.class, new AttendanceExcelListener());
        final ExcelReaderSheetBuilder sheet = read.sheet();
        sheet.doRead();
        final long ed = System.currentTimeMillis();
        log.info("考勤数据导入读取Excel用时：{}ms", ed - st);
        final long st1 = System.currentTimeMillis();
        // 批量保存
        ams.saveBatch(atList);
        final long ed1 = System.currentTimeMillis();
        log.info("考勤数据批量插入用时：{}ms", ed1 - st1);
        atList.clear();
    }

    /**
     * 考勤导入Excel操作的内部类
     */
    class AttendanceExcelListener extends AnalysisEventListener<AtteUploadVo> {

        @SneakyThrows
        @Override
        public void invoke(AtteUploadVo atteUploadVo, AnalysisContext analysisContext) {
            User user = null;
            user = SystemCache.USER_INFO_CACHE.get(atteUploadVo.getMobile());
            if (user == null) {
                final Object o = redisTemplate.boundHashOps(SystemConstant.REDIS_USER_LIST).get(atteUploadVo.getMobile());
                user = JSON.parseObject(JSON.toJSONString(o), User.class);
                SystemCache.USER_INFO_CACHE.put(atteUploadVo.getMobile(), user);
            }
            Attendance attendance = new Attendance(atteUploadVo, user);
            attendance.setDay(atteUploadVo.getAtteDate());
            // 设置考勤状态
            if (holidays.contains(attendance.getDay())) {
                attendance.setAdtStatus(AttendanceStatusEnum.REST.getValue());
            } else if (DateUtil.isWeekend(DateUtil.parse(attendance.getDay(),
                                                         DatePattern.PURE_DATE_PATTERN)) && !wordingDays.contains(attendance.getDay())) {
                attendance.setAdtStatus(AttendanceStatusEnum.REST.getValue());
            } else {
                String morningStartTime = "";
                String afternoonEndTime = "";
                for (AttendanceConfig attendanceConfig : atcList) {
                    if (user.getCompanyId().equals(attendanceConfig.getCompanyId())
                            && user.getDepartmentId().equals(attendanceConfig.getDepartmentId())) {
                        morningStartTime = attendanceConfig.getMorningStartTime();
                        afternoonEndTime = attendanceConfig.getAfternoonEndTime();
                    }
                }
                final String inTime = DateUtil.format(atteUploadVo.getInTime(), DatePattern.NORM_TIME_PATTERN);
                final String outTime = DateUtil.format(atteUploadVo.getOutTime(), DatePattern.NORM_TIME_PATTERN);
                final Boolean isLate = DateUtils.compareTimeAfter(inTime, morningStartTime);
                final Boolean isEarly = DateUtils.compareTimeAfter(afternoonEndTime, outTime);
                if (!isLate && !isEarly) {
                    attendance.setAdtStatus(AttendanceStatusEnum.NORMAL.getValue());
                }
                if (isLate) {
                    // 迟到
                    attendance.setAdtStatus(AttendanceStatusEnum.LATE.getValue());
                }
                if (isEarly) {
                    // 早退
                    attendance.setAdtStatus(AttendanceStatusEnum.LEAVE_EARLY.getValue());
                }
//                if (isLate && isEarly) {
//                    // 迟到早退
//                    attendance.setAdtStatus(AttendanceStatusEnum.LEAVE_EARLY_AND_LATE.getValue());
//                }
            }
            attendance.setId(IdWorker.getIdStr());
            attendance.setCreateDate(new Date());
            atList.add(attendance);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

    }

}
