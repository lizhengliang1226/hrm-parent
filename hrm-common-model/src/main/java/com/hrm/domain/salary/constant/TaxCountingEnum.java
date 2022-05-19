package com.hrm.domain.salary.constant;


public enum TaxCountingEnum {
    /**
     * 税前
     */
    BEFORE_TAX(1, "税前"),
    /**
     * 税后
     */
    AFTER_TAX(2, "税后");

    private final Integer key;
    private final String value;

    TaxCountingEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static TaxCountingEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (TaxCountingEnum temp : TaxCountingEnum.values()) {
            if (temp.getKey().equals(key)) {
                return temp;
            }
        }
        return null;
    }

    public static TaxCountingEnum getEnumByValue(String value) {
        if (value.isEmpty()) {
            return null;
        }
        for (TaxCountingEnum temp : TaxCountingEnum.values()) {
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
