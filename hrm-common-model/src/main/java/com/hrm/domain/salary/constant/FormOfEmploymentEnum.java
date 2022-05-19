package com.hrm.domain.salary.constant;

public enum FormOfEmploymentEnum {
    /**
     * 正式
     */
    OFFICIAL(1, "正式"),
    /**
     * 实习
     */
    FIELDWORK(2, "实习"),
    /**
     * 劳务
     */
    SERVICE(3, "劳务"),
    /**
     * 疑问
     */
    QUESTION(4, "疑问"),
    /**
     * 返聘
     */
    REHIRING(5, "返聘"),
    /**
     * 外包
     */
    OUTSOURCING(6, "外包");

    private final Integer key;
    private final String value;

    FormOfEmploymentEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static FormOfEmploymentEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (FormOfEmploymentEnum temp : FormOfEmploymentEnum.values()) {
            if (temp.getKey().equals(key)) {
                return temp;
            }
        }
        return null;
    }

    public static FormOfEmploymentEnum getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        for (FormOfEmploymentEnum temp : FormOfEmploymentEnum.values()) {
            if (temp.getValue().equals(value)) {
                return temp;
            }
        }
        return null;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
