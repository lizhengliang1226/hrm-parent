package com.hrm.common.shiro.session;

import cn.hutool.core.util.StrUtil;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @Description 自定义session管理器
 * @Author LZL
 * @Date 2022/3/14-6:35
 */
public class CustomSessionManager extends DefaultWebSessionManager {
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 获取请求头中的数据
        String sid = WebUtils.toHttp(request).getHeader("Authorization");
        System.out.println("获取的sid" + sid);
        // 没有sid，生成
        if (StrUtil.isEmpty(sid)) {
            return super.getSessionId(request, response);
        } else {
            //返回sid
            sid = sid.replace("Bearer ", "");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sid);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sid;
        }
    }
}
