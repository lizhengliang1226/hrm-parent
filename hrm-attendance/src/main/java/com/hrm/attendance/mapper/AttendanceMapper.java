package com.hrm.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrm.domain.attendance.bo.AtteItemBO;
import com.hrm.domain.attendance.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 考勤mapper
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/18-0:32
 */
@Repository
@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {
    List<AtteItemBO> findMonthAtteData(Map map);

    Integer countsOfUserAtte(Map map);
}
