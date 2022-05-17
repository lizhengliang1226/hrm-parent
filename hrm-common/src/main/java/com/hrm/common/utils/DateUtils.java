package com.hrm.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * 时间工具类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/17-19:56
 */
public class DateUtils {
    /**
     * 计算两个时间（不含日期，只有时间）之间的插差，例如 18:30:00 和19:30:00
     *
     * @param target     开始时间
     * @param sourceDate 结束时间
     * @return 差值
     */
    public static Boolean compareTimeAfter(String target, String sourceDate) {
        final DateTime start1 = DateUtil.parse(target, DatePattern.NORM_TIME_PATTERN);
        final DateTime end2 = DateUtil.parse(sourceDate, DatePattern.NORM_TIME_PATTERN);
        return start1.after(end2);
    }

    public static String[] getMonthEveryDay(String month, String pattern) {
        final int i = DateUtil.endOfMonth(DateUtil.parse(month, pattern)).dayOfMonth();
        String[] r = new String[i];
        for (int j = 1; j <= i; j++) {
            if (j < 10) {
                r[j - 1] = month + "0" + j;
                continue;
            }
            r[j - 1] = month + j;
        }
        return r;
    }

    public static int getMonthDays(String month, String pattern) {
        return DateUtil.endOfMonth(DateUtil.parse(month, pattern)).dayOfMonth();
    }

}
