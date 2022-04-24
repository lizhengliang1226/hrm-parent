package com.hrm.common.utils;

import cn.hutool.core.codec.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件操作工具类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/4/24-23:41
 */
public class FileUtils {
    /**
     * 文件转base64的url
     *
     * @param attachment 文件
     * @return base64字符串
     * @throws IOException 异常
     */
    public static String fileToDataUrl(MultipartFile attachment) throws IOException {
        // 图片base64编码
        final String encode = Base64.encode(attachment.getBytes());
        String url = "data:" + attachment.getContentType() + ";base64," + encode;
        return url;
    }
}
