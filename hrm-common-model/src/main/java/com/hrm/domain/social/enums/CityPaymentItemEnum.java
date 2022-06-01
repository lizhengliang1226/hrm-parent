package com.hrm.domain.social.enums;

/**
 * 城市社保缴费项枚举
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/20-16:17
 */
public enum CityPaymentItemEnum {
    /**
     * 养老保险
     */
    ENDOWMENT_INSURANCE("1", "养老保险"),
    /**
     * 医疗保险
     */
    MEDICAL_INSURANCE("2", "医疗保险"),
    /**
     * 失业保险
     */
    UNEMPLOYMENT_INSURANCE("3", "失业保险"),
    /**
     * 工伤保险
     */
    INDUCTRIAL_INJURY_INSURANCE("4", "工伤保险"),
    /**
     * 生育保险
     */
    BIRTH_INSURANCE("5", "生育保险"),
    /**
     * 大病保险
     */
    SERIOUS_ILLNESS_INSURANCE("6", "大病保险");

    /**
     * 类型值
     */
    private final String value;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 类型描述
     */
    private final String name;

    CityPaymentItemEnum(String s, String name) {
        this.value = s;
        this.name = name;
    }
}
