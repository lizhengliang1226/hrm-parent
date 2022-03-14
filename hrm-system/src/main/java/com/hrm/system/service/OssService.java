package com.hrm.system.service;

import java.util.Map;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/11-3:05
 */
public interface OssService {
    /**
     * OSS云存储的后端签名接口
     *
     * @return
     */
    Map<String, String> policy();
}
