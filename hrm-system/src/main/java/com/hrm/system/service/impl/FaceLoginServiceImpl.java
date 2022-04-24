package com.hrm.system.service.impl;


import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.hrm.common.utils.FileUtils;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.system.User;
import com.hrm.domain.system.response.FaceLoginResult;
import com.hrm.domain.system.response.QRCode;
import com.hrm.system.dao.UserDao;
import com.hrm.system.service.FaceLoginService;
import com.hrm.system.utils.TencentAiFaceUtil;
import com.lzl.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 17314
 */
@Slf4j
@Service
public class FaceLoginServiceImpl implements FaceLoginService {

    @Value("${qr.url}")
    private String url;
    @Value("${tencent-face.groupId}")
    private String groupId;
    @Autowired
    private TencentAiFaceUtil tencentAiFaceUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserDao userDao;

    @Override
    public QRCode getQRCode() throws Exception {
        QrConfig qrConfig = new QrConfig();
        qrConfig.setWidth(200);
        qrConfig.setHeight(200);
        String code = IdWorker.getIdStr();
        log.info("生成二维码的访问url：{}", url + "?code=" + code);
        FaceLoginResult result = new FaceLoginResult(SystemConstant.NOT_SCAN);
        redisTemplate.boundValueOps(getCacheKey(code)).set(result, 10, TimeUnit.MINUTES);
        String jpg = QrCodeUtil.generateAsBase64(url + "?code=" + code, qrConfig, "jpg");
        log.info("生成的二维码：{}", jpg);
        QRCode qrCode = new QRCode();
        qrCode.setFile(jpg);
        qrCode.setCode(code);
        return qrCode;
    }

    @Override
    public FaceLoginResult checkQRCode(String code) {
        final String cacheKey = getCacheKey(code);
        return (FaceLoginResult) redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    public String loginByFace(String code, MultipartFile attachment) throws Exception {
        // 图片base64编码
        String url = FileUtils.fileToDataUrl(attachment);
        // 查询人脸库
        final String userId = tencentAiFaceUtil.searchPerson(groupId, url);
        // 设置登录状态为未登录
        FaceLoginResult result = new FaceLoginResult(SystemConstant.NOT_LOGIN);
        if (userId != null) {
            // 登录成功
            final User user = userDao.findById(userId).get();
            // 模拟登录
            Subject subject = SecurityUtils.getSubject();
            subject.login(new UsernamePasswordToken(user.getMobile(), user.getPassword()));
            String sessionId = (String) subject.getSession().getId();
            // 设置状态为已登陆
            result.setState(SystemConstant.IS_LOGIN);
            result.setToken(sessionId);
            result.setUserId(userId);
        }
        // 把登录状态存到redis
        redisTemplate.boundValueOps(getCacheKey(code)).set(result, 10, TimeUnit.MINUTES);
        return userId;
    }

    @Override
    public boolean isExistFace(MultipartFile file) throws IOException {
        String url = FileUtils.fileToDataUrl(file);
        return tencentAiFaceUtil.isFace(url);
    }

    private String getCacheKey(String code) {
        return "qrcode_" + code;
    }
}
