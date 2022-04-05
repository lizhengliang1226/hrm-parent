package com.hrm.system.service;

import java.util.Map;

/**
 * oss服务
 *
 * @author LZL
 * @date 2022/3/11-3:05
 */
public interface OssService {
    /**
     * OSS云存储的后端签名接口
     *
     * @return 签名信息
     */
    Map<String, String> policy();
}
