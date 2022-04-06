package com.hrm.common.entity;

/**
 * 公共返回码
 *
 * @author 17314
 */
public enum ResultCode {
    /**
     * 操作成功返回码
     */
    SUCCESS(true, 10000, "操作成功！"),
    //---系统错误返回码-----
    /**
     * 操作失败返回码
     */
    FAIL(false, 10001, "操作失败！"),
    /**
     * 未登录返回码
     */
    UNAUTHENTICATED(false, 10002, "您还未登录！"),
    /**
     * 权限不足返回码
     */
    UNAUTHORIZED(false, 10003, "权限不足！"),
    /**
     * 服务器错误返回码
     */
    SERVER_ERROR(false, 99999, "抱歉，系统繁忙，请稍后重试！"),
    /**
     * 登录过期返回码
     */
    LOGIN_STATUS_EXPIRED(false, 10004, "登录状态过期，请重新登录！"),
    //---用户操作返回码----
    /**
     * 登陆失败返回码
     */
    LOGIN_FAIL(false, 20001, "用户名或密码错误！");
    //---企业操作返回码----

    //---权限操作返回码----
    //---其他操作返回码----

    /**
     * 操作是否成功
     */
    boolean success;
    /**
     * 操作代码
     */
    int code;
    /**
     * 提示信息
     */
    String message;

    ResultCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
