package com.hrm.domain.attendance.vo;

import lombok.Data;

@Data
public class ArchiveMonthlyVO {

    /**
     * 编号
     */
    private String id;

    /**
     * 月份
     */
    private String month;

    /**
     * 总人数
     */
    private Integer totalNumber;

    /**
     * 全勤人数
     */
    private Integer fullAttendance;

    /**
     * 不考勤人数
     */
    private Integer noAttendance;
}
