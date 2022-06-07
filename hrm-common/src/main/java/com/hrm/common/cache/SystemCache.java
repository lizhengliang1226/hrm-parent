package com.hrm.common.cache;

import com.hrm.domain.attendance.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统缓存
 *
 * @author LZL
 * @version v1.0
 * @date 2022/6/4-21:32
 */
public class SystemCache {
    public static Map<String, User> USER_INFO_CACHE = new HashMap<>(64);

}
