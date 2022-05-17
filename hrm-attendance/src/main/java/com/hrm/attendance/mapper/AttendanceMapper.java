package com.hrm.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrm.domain.attendance.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考勤mapper
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/18-0:32
 */
@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {
}
