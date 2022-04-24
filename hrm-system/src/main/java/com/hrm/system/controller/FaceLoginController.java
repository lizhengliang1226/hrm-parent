package com.hrm.system.controller;


import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.system.response.FaceLoginResult;
import com.hrm.domain.system.response.QRCode;
import com.hrm.system.service.FaceLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 17314
 */
@Slf4j
@RestController
@RequestMapping("/sys/faceLogin")
@Api(tags = "人脸登录")
public class FaceLoginController {
    @Autowired
    private FaceLoginService faceLoginService;

    /**
     * 获取刷脸登录二维码
     */
    @GetMapping(value = "/qrcode")
    @ApiOperation(value = "获取刷脸登录二维码")
    public Result qrcode() throws Exception {
        final QRCode qrCode = faceLoginService.getQRCode();
        return new Result(ResultCode.SUCCESS, qrCode);
    }

    /**
     * 检查二维码：登录页面轮询调用此方法，根据唯一标识code判断用户登录情况
     */
    @GetMapping(value = "/qrcode/{code}")
    @ApiOperation(value = "检查二维码状态")
    public Result qrcodeCeck(@PathVariable(name = "code") String code) throws Exception {
        final FaceLoginResult result = faceLoginService.checkQRCode(code);
        return new Result(ResultCode.SUCCESS, result);
    }

    /**
     * 人脸登录：根据落地页随机拍摄的面部头像进行登录
     * 根据拍摄的图片调用百度云AI进行检索查找
     */
    @PostMapping(value = "/{code}")
    @ApiOperation(value = "人脸登录功能")
    public Result loginByFace(@PathVariable(name = "code") String code, @RequestParam(name = "file") MultipartFile attachment) throws Exception {
        final String userId = faceLoginService.loginByFace(code, attachment);
        return userId != null ? new Result(ResultCode.SUCCESS) : new Result(ResultCode.LOGIN_FAIL);
    }


    /**
     * 图像检测，判断图片中是否存在面部头像
     */
    @PostMapping(value = "/checkFace")
    @ApiOperation(value = "检查图片是否存在人脸")
    public Result checkFace(@RequestParam(name = "file") MultipartFile file) throws Exception {
        return faceLoginService.isExistFace(file) ? Result.SUCCESS() : new Result(ResultCode.IMG_NO_FACE);
    }
}
