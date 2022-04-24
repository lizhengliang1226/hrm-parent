package com.hrm.domain.constant;

/**
 * 系统字段名的常量值
 *
 * @author LZL
 * @version v1.0
 * @date 2022/3/30-19:49
 */
public class SystemConstant {
    public static final String COMPANY_ID = "companyId";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DEPARTMENT_ID = "departmentId";
    /**
     * 有分配部门
     */
    public static final String HAS_DEPT = "hasDept";
    /**
     * 在职状态
     */
    public static final String IN_SERVICE_STATUS = "inServiceStatus";
    /**
     * 父id
     */
    public static final String PID = "pid";
    /**
     * 启用状态
     */
    public static final String EN_VISIBLE = "enVisible";
    /**
     * 权限查询类型
     */
    public static final String TYPE = "type";
    /**
     * 权限查询标志，0-查菜单，按钮权限
     */
    public static final String PERM_QUERY_FLAG = "0";
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";
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
    /**
     * 菜单
     */
    public static final int PY_MENU = 1;
    /**
     * 功能按钮
     */
    public static final int PY_POINT = 2;
    /**
     * API
     */
    public static final int PY_API = 3;
    /**
     * 未登录
     */
    public static final String NOT_LOGIN = "0";
    /**
     * 已登陆
     */
    public static final String IS_LOGIN = "1";
    /**
     * 二维码未扫描
     */
    public static final String NOT_SCAN = "-1";
}
