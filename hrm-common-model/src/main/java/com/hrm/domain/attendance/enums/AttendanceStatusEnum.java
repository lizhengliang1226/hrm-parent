package com.hrm.domain.attendance.enums;

/**
 * 考勤状态枚举
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/17-18:52
 */
public enum AttendanceStatusEnum {
    /**
     * 正常
     */
    NORMAL(1, "正常"),
    /**
     * 旷工
     */
    ABSENTEEISM(2, "旷工"),
    /**
     * 迟到
     */
    LATE(3, "迟到"),
    /**
     * 早退
     */
    LEAVE_EARLY(4, "早退"),

    /**
     * 外出
     */
    GO_OUT(5, "外出"),
    /**
     * 出差
     */
    EVECTION(6, "出差"),
    /**
     * 年假
     */
    ANNUAL_VACATION(7, "年假"),
    /**
     * 事假
     */
    LEAVE_PERSONAL(8, "事假"),
    /**
     * 病假
     */
    SICK_LEAVE(9, "病假"),
    /**
     * 婚假
     */
    MARRIAGE_LEAVE(10, "婚假"),
    /**
     * 产假
     */
    MATERNITY_LEAVE(11, "产假"),
    /**
     * 丧假
     */
    FUNERAL_LEAVE(12, "丧假"),
    /**
     * 陪产假
     */
    PATERNITY_LEAVE(13, "陪产假"),
    /**
     * 探亲假
     */
    HOME_LEAVE(14, "探亲假"),
    /**
     * 工伤假
     */
    INJURY_LEAVE(15, "工伤假"),
    /**
     * 调休
     */
    ADJUSTABLE_LEAVE(16, "调休"),
    /**
     * 产检假
     */
    PRENATAL_CHECK_LEAVE(17, "产检假"),
    /**
     * 流产假
     */
    ABORTION_LEAVE(18, "流产假"),
    /**
     * 长期病假
     */
    LONG_SICK_LEAVE(19, "长期病假"),
    /**
     * 补签
     */
    RETROACTIVE(20, "补签"),
    /**
     * 未打卡
     */
    NOT_CLOCK(23, "未打卡"),
    /**
     * 休息
     */
    REST(21, "休息"),
    /**
     * 迟到和早退
     */
    LEAVE_EARLY_AND_LATE(22, "迟到早退"),
    ;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 类型描述
     */
    private final String desc;

    AttendanceStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
