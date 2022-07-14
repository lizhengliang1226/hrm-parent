package com.hrm.common.handler;

import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.exception.CommonException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义公共异常处理器
 *
 * @author LZL
 * @date 2022/1/12-12:54
 */
@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, HttpServletResponse response, Exception e) {
        e.printStackTrace();
        if (e.getClass() == CommonException.class) {
            //类型转型
            CommonException ce = (CommonException) e;
            return new Result(ce.getResultCode());
        } else if (e.getClass() == UnauthorizedException.class) {
            return new Result(ResultCode.UNAUTHORIZED);
        } else if (e.getClass() == AuthenticationException.class) {
            return new Result(ResultCode.UNAUTHORIZED);
        } else {
            return new Result(ResultCode.SERVER_ERROR);
        }
    }
}
