package com.hrm.domain.attendance.bo;

import lombok.Data;

/**
 * 用户考勤记录bo
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/22-0:10
 */
@Data
public class UserAtteBo {

    private String userId;
    private String name;
    private String workNumber;
    private String departmentName;
    private String mobile;
    private String normalDays;
    private String absenceDays;
    private String workCity;
    private String sickLeaveDays;
    private String longSickLeaveDays;
    /**
     * 探亲假
     */
    private String homeLeaveDays;
    private String funeralLeaveDays;
    /**
     * 产假
     */
    private String maternityLeaveDays;
    /**
     * 陪产假
     */
    private String paternityLeaveDays;
    private String marriageLeaveDays;
    private String laterTimes;
    private String earlyTimes;
    private String leaveDays;
    private String dayOffLeaveDays;
    private String actualAtteOfficialDays;
    private String salaryOfficialDays;
}
