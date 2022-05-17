package com.hrm.attendance.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrm.attendance.mapper.AttendanceMapper;
import com.hrm.domain.attendance.entity.Attendance;
import org.springframework.stereotype.Service;

/**
 * 考勤mappr服务
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/18-1:11
 */
@Service
public class AttendanceMapperService extends ServiceImpl<AttendanceMapper, Attendance> implements AtteService {
}
