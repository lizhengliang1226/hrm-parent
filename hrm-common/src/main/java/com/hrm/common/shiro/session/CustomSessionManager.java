package com.hrm.common.shiro.session;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义session管理器
 *
 * @author LZL
 * @date 2022/3/14-6:35
 */
@Slf4j
public class CustomSessionManager extends DefaultWebSessionManager {


    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 获取请求头中的数据
        String sid = WebUtils.toHttp(request).getHeader("Authorization");
        log.info("获取的sid: {}", sid);
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
