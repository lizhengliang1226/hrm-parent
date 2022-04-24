package com.hrm.system.service;

import com.hrm.domain.system.response.FaceLoginResult;
import com.hrm.domain.system.response.QRCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 人脸登录服务
 *
 * @author LZL
 * @version v1.0
 * @date 2022/4/23-1:22
 */
public interface FaceLoginService {
    /**
     * 创建二维码
     *
     * @return 创建的二维码对象
     * @throws Exception 异常
     */
    public QRCode getQRCode() throws Exception;

    /**
     * 查询是否登录成功
     *
     * @param code 二维码id
     * @return 登录结果
     */
    public FaceLoginResult checkQRCode(String code);

    /**
     * 人脸登录
     *
     * @param code       二维码id
     * @param attachment 人脸文件
     * @return 登录成功的用户id
     * @throws Exception 异常
     */
    public String loginByFace(String code, MultipartFile attachment) throws Exception;

    /**
     * 判断图片是否存在人脸
     *
     * @param file 图片
     * @return 是否存在
     * @throws IOException 异常
     */
    public boolean isExistFace(MultipartFile file) throws IOException;
}
