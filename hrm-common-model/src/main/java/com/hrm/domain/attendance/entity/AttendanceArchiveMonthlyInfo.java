package com.hrm.domain.attendance.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.hrm.domain.attendance.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 考勤归档信息详情表
 */
@TableName("atte_archive_monthly_info")
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "atte_archive_monthly_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceArchiveMonthlyInfo extends BaseEntity implements Serializable {

    @Id
    private String id;
    private BigDecimal absenteeismDays;
    private String userId;
    private String atteArchiveMonthlyId;
    private String name;

    private String workNumber;
    private String mobile;
//    private String atteSolution;

    private String department;
    private String workCity;
    private String yearLeaveDays;

    private String leaveDays;
    private String sickLeaveDays;
    private String longSickLeaveDays;

    private String marriageLeaveDays;
    private String funeralLeaveDays;
    /**
     * 产假
     */
    private String maternityLeaveDays;

//  private String rewardMaternityLeaveDays;

    /**
     * 陪产假
     */
    private String paternityLeaveDays;
    /**
     * 探亲假
     */
    private String homeLeaveDays;

    /**
     * 工伤假
     */
    private String accidentialLeaveDays;
    private String dayOffLeaveDays;
    /**
     * 产检假
     */
    private String doctorOffLeaveDays;

    /**
     * 流产假
     */
    private String abortionLeaveDays;
    private String normalDays;
    private String outgoingDays;

    private String onBusinessDays;
    private String laterTimes;
    private String earlyTimes;


    /**
     * 日均时长（自然日）
     */
    private String hoursPerDays;
    private String hoursPerWorkDay;

    private String hoursPerRestDay;
    private String clockRate;
    private String absenceDays;

    private Integer isFullAttendanceint;
    /**
     * 实际出勤天数非正式
     */
//    private String actualAtteUnofficialDays;
    /**
     * 实际出勤天数，正式
     */
    private String actualAtteOfficialDays;

    /**
     * 应出勤工作日
     */
    private String workingDays;
    /**
     * 计薪标准
     */
    private String salaryStandards;
    /**
     * 计薪天数调整
     */

    private String salaryAdjustmentDays;

    /**
     * 工作时长
     */
    private String workHour;

    /**
     * 计薪天数（非正式）
     */
    private String salaryUnofficialDays;
    /**
     * 计薪天数（正式）
     */
    private String salaryOfficialDays;
    /**
     * 归档日期
     */
    private String archiveDate;
    /**
     * 补签
     */
    private Integer retrocative;
    private Integer rest;
    private Integer notClock;
    private Integer leaveEarlyAndLate;

    public AttendanceArchiveMonthlyInfo(User user) {
        this.userId = user.getId();
        this.name = user.getUsername();
        this.workNumber = user.getWorkNumber();
        this.department = user.getDepartmentName();
        this.mobile = user.getMobile();
    }


    public void setStatisData(Map<String, Object> map) {
        final int days = (int) map.get("at0");
        // 正常旷工迟到早退事假调休
        this.normalDays = (String) map.get("at1").toString();
        this.absenceDays = (String) map.get("at2").toString();
        this.laterTimes = (String) map.get("at3").toString();
        this.earlyTimes = (String) map.get("at4").toString();
        this.leaveDays = (String) map.get("at8").toString();
        this.dayOffLeaveDays = (String) map.get("at16").toString();
        // 应出勤天数 at0-at21  缺勤at8+at9+at10+at11+at12+at14+at7+at15+at2
        //实际

        this.actualAtteOfficialDays = this.normalDays;
        this.salaryOfficialDays = this.normalDays;
    }
}
