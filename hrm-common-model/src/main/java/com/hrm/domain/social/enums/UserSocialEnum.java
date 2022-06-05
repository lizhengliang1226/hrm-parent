package com.hrm.domain.social.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 城市社保缴费项枚举
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/20-16:17
 */
public enum UserSocialEnum {
    /**
     * 养老保险
     */
    CITY_TOWN("1", "本市城镇"),
    /**
     * 医疗保险
     */
    CITY_RURAL("2", "本市农村"),
    /**
     * 失业保险
     */
    OUTSIDE_TOWN_MOUND("3", "外阜城镇"),
    /**
     * 工伤保险
     */
    MOUND_RURAL("4", "外阜农村"),
    /**
     * 生育保险
     */
    FIRST_ACCOUNT("1", "首次开户"),
    /**
     * 大病保险
     */
    NON_FIRST_ACCOUNT("2", "非首次开户");

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

    private static final Map<String, UserSocialEnum> LOOKUP = new HashMap<>();

    //初始化
    static {

        for (UserSocialEnum deductionEnum : EnumSet.allOf(UserSocialEnum.class)) {

            LOOKUP.put(deductionEnum.name, deductionEnum);
        }
    }

    /**
     * 获取Map的值
     *
     * @param name
     * @return 值
     */
    public static UserSocialEnum lookup(String name) {
        return LOOKUP.get(name);
    }

    /**
     * 类型描述
     */
    private final String name;

    UserSocialEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }
}
