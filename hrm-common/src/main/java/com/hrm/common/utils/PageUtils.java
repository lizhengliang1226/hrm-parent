package com.hrm.common.utils;

import java.util.Map;

/**
 * 分页操作工具
 *
 * @author LZL
 * @version v1.0
 * @date 2022/6/1-11:28
 */
public class PageUtils {
    public static void doPage(Map map) {
        Integer page = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pagesize");
        final String keyword = (String) map.get("keyword");
        if (keyword != null && keyword.length() > 0) {
            map.put("keyword", "%" + keyword + "%");
        }
        map.put("page", (page - 1) * pageSize);
    }
}
