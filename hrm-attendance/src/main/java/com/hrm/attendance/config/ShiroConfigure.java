package com.hrm.attendance.config;

import com.hrm.common.shiro.realm.HrmRealm;
import com.hrm.common.shiro.session.CustomSessionManager;
import lombok.Setter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 *
 * @author LZL
 * @date 2022/3/14-5:51
 */
@Configuration
@Setter
@ConfigurationProperties(prefix = "spring.redis")
public class ShiroConfigure {

    private String host;
    private int port;
    private String password;
    /**
     * 匿名访问
     */
    private static final String ANON_ACCESS = "anon";
    /**
     * 授权访问
     */
    private static final String AUTH_ACCESS = "authc";

    /**
     * 配置自定义realm
     *
     * @return
     */
    @Bean
    public HrmRealm getRealm() {
        return new HrmRealm();
    }

    /**
     * 配置安全管理器
     *
     * @param realm
     * @return
     */
    @Bean
    public SecurityManager getSecurityManager(HrmRealm realm) {
        //使用默认的安全管理器
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        //将自定义的realm交给安全管理器统一调度管理
        securityManager.setRealm(realm);
        return securityManager;
    }

    /**
     * 配置shiro注解支持
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        //1.创建shiro过滤器工厂
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        filterFactory.setSecurityManager(securityManager);
        //3.通用配置
        // 登录失败后去的请求
        filterFactory.setLoginUrl("/authError?code=1");
        // 未授权去的请求
        filterFactory.setUnauthorizedUrl("/authError?code=2");
        //4.配置过滤器集合
        /**
         * key  ：访问连接
         *      支持通配符的形式
         * value：过滤器类型
         *      shiro常用过滤器
         *          anno    ：匿名访问（表明此链接所有人可以访问）
         *          authc   ：认证后访问（表明此链接需登录认证成功之后可以访问）
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 匿名访问
        filterMap.put("/authError", ANON_ACCESS);
        //认证之后访问（登录之后可以访问）
        filterMap.put("/**", AUTH_ACCESS);
        //5.设置过滤器
        filterFactory.setFilterChainDefinitionMap(filterMap);
        return filterFactory;
    }

    /**
     * shiro session的管理
     */
    public DefaultWebSessionManager sessionManager() {
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        // 会话超时时间，单位：毫秒
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        // 定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话
        sessionManager.setSessionValidationInterval(60 * 60 * 1000);
        // 是否开启定时清理失效会话
        sessionManager.setSessionValidationSchedulerEnabled(true);
        return sessionManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 配置shiro redisManager
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager缓存 redis实现
     *
     * @return RedisCacheManager
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }
}
