package com.hrm.attendance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 17314
 */
@Mapper
public interface ArchiveMonthlyInfoMapper
        extends BaseMapper<AttendanceArchiveMonthlyInfo> {
    /**
     * 查询用户的考勤信息，显示各种天数
     *
     * @param map
     * @return
     */
    List<AttendanceArchiveMonthlyInfo> userAtteDays(Map map);

    /**
     * 查询考勤记录数
     *
     * @param date 月份
     * @return
     */
    Integer countsOfAtteDatabase(@Param("month") String date, @Param("companyId") String companyId);

    /**
     * 删除考勤明细
     *
     * @param id
     */
    @Delete("delete from atte_archive_monthly_info where atte_archive_monthly_id=#{id}")
    void removeByAtteArchiveMonthlyId(String id);
}