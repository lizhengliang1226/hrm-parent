package com.hrm.attendance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArchiveMonthlyInfoMapper
        extends BaseMapper<AttendanceArchiveMonthlyInfo> {

}