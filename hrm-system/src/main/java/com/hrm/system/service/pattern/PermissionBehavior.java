package com.hrm.system.service.pattern;

/**
 * 策略模式的策略接口
 * 权限操作行为接口，由用户指定对权限做什么操作
 *
 * @author LZL
 * @version v1.0
 * @date 2022/3/31-3:51
 */
@FunctionalInterface
public interface PermissionBehavior {
    /**
     * 权限行为策略
     *
     * @param permOperate 权限操作
     * @throws Exception 异常
     */
    void apply(AbstractPermissionOperateBehavior permOperate) throws Exception;
}
