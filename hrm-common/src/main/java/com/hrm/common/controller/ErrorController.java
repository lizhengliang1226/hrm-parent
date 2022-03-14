package com.hrm.common.controller;

import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/14-7:49
 */
@RestController
public class ErrorController {
    @GetMapping("authError")
    public Result authError(Integer code){
        // 1是未登录 2是权限不足
        return code==1?new Result(ResultCode.UNAUTHENTICATED):new Result(ResultCode.UNAUTHORIZED);
    }
}
