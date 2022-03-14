package com.hrm.common.entity;

/**
 * @Description 用户级别
 * @Author LZL
 * @Date 2022/3/12-0:24
 */

public class UserLevel {

    /**
     * saas系统管理员，最高权限
     */
    public static final String SAAS_ADMIN = "saasAdmin";
    /**
     * 企业管理员，管理单个企业
     */
    public static final String COMPANY_ADMIN = "coAdmin";
    /**
     * 普通用户，根据角色获得权限
     */
    public static final String NORMAL_USER = "user";
}
