package com.hrm.system.shiro.realm;

import com.hrm.common.entity.UserLevel;
import com.hrm.common.shiro.realm.HrmRealm;
import com.hrm.domain.system.Permission;
import com.hrm.domain.system.User;
import com.hrm.domain.system.response.ProfileResult;
import com.hrm.system.service.PermissionService;
import com.hrm.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/14-6:49
 */
@Slf4j
public class UserRealm extends HrmRealm {

    private UserService userService;
    private PermissionService permissionService;

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户信息认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取用户名和密码
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        final String password = new String(usernamePasswordToken.getPassword());
        final String mobile = usernamePasswordToken.getUsername();
        final User user = userService.findByMobile(mobile);
        // 构建安全数据
        ProfileResult profileResult = null;
        if (user != null && user.getPassword().equals(password)) {
            if (UserLevel.NORMAL_USER.equals(user.getLevel())) {
                // 普通用户，直接根据用户拥有的角色获取权限
                profileResult = new ProfileResult(user);
            } else {
                Map<String, Object> map = new HashMap<>(1);
                if (UserLevel.COMPANY_ADMIN.equals(user.getLevel())) {
                    // 企业管理员。设置企业可见性为1查询权限给用户
                    map.put("enVisible", "1");
                }
                List<Permission> allPerm = permissionService.findAll(map);
                profileResult = new ProfileResult(user, allPerm);
            }
            // 安全数据，密码，realm
            final Map<String, Object> roles = profileResult.getRoles();
            final Set<String> apis = (Set<String>) roles.get("apis");
            return new SimpleAuthenticationInfo(profileResult, password, getName());
        }
        return null;
    }
}
