package com.hrm.common.shiro.realm;

import com.hrm.domain.system.response.ProfileResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/14-6:46
 */
public class HrmRealm extends AuthorizingRealm {

    @Override
    public void setName(String name) {
        super.setName("hrmRealm");
    }

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取安全数据
        ProfileResult profileResult = (ProfileResult) principalCollection.getPrimaryPrincipal();
        // 获取权限信息
        Set<String> apis = (Set<String>) profileResult.getRoles().get("apis");
        // 构造权限数据
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(apis);
        return info;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
