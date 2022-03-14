package com.hrm.common.controller;

import com.hrm.domain.system.response.ProfileResult;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/7-20:28
 */
public class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;

    /**
     * 使用shiro获取安全数据
     * @param request
     * @param response
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
        }

    }

}
