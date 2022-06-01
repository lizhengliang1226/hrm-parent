package com.hrm.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrm.domain.attendance.entity.ArchiveMonthly;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考勤归档主档接口
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/21-23:05
 */
@Mapper
public interface ArchiveMonthMapper extends BaseMapper<ArchiveMonthly> {
}
