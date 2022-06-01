package com.hrm.common.controller;

import com.hrm.domain.system.response.ProfileResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BaseController，所有控制器的父类，会事先将session中的安全数据读出来进行赋值
 *
 * @author LZL
 * @date 2022/3/7-20:28
 */
@Slf4j
public class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;
    protected String username;
    protected String mobile;
    protected String managerId;

    /**
     * 使用shiro获取安全数据
     * 前置处理器
     * @param request  请求
     * @param response 响应
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        // 获取session中的安全数据
        final Subject subject = SecurityUtils.getSubject();
        final PrincipalCollection previousPrincipals = subject.getPrincipals();
        if (previousPrincipals != null&&!previousPrincipals.isEmpty()) {
            final ProfileResult profileResult = (ProfileResult) previousPrincipals.getPrimaryPrincipal();
            this.companyId = profileResult.getCompanyId();
            this.companyName = profileResult.getCompany();
            this.username = profileResult.getUsername();
            this.mobile = profileResult.getMobile();
            this.managerId = profileResult.getManagerId();
        }

    }

}
