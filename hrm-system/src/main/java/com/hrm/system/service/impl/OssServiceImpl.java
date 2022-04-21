package com.hrm.system.service.impl;

import com.hrm.common.utils.AliOssUploadUtils;
import com.hrm.system.service.OssService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OSS服务
 *
 * @author LZL
 * @date 2022/3/11-3:07
 */
@Service
@ConfigurationProperties(prefix = "oss")
@Setter
@Slf4j
public class OssServiceImpl implements OssService {

    private String accessId;
    private String accessKeySecret;
    private String endpoint;
    private String bucket;
    private String host;

    @Override
    public Map<String, String> policy() {
        return AliOssUploadUtils.getSignature(accessId, accessKeySecret, endpoint, bucket, host, 30, TimeUnit.SECONDS);
    }
}
