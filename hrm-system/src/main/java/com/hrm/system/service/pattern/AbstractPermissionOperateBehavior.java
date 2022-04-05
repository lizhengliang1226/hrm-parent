package com.hrm.system.service.pattern;

import lombok.Setter;

import java.util.Map;

/**
 * 责任链模式中的抽象类，定义责任链的传播方式和操作方法
 * 使用策略模式由用户指定操作方法
 *
 * @author LZL
 * @version v1.0
 * @date 2022/3/31-2:39
 */
public abstract class AbstractPermissionOperateBehavior<T> {
    protected int type;
    protected T dao;
    @Setter
    protected AbstractPermissionOperateBehavior next;

    public void permOperate(int type, PermissionBehavior permOperate) throws Exception {
        // 判断操作的权限类型，如果相等就调用apply对数据操作
        if (this.type == type) {
            permOperate.apply(this);
            return;
        }
        if (next != null) {
            // 还有下一个责任链的话就传播到下一个
            next.permOperate(type, permOperate);
        }
    }

    public AbstractPermissionOperateBehavior(int type, T dao) {
        this.type = type;
        this.dao = dao;
    }

    /**
     * 责任链中权限操作的更新或保存行为
     *
     * @param map 权限信息
     * @param id  权限id
     * @throws Exception 异常
     */
    abstract public void permSaveOrUpdateBehavior(Map<String, Object> map, String id) throws Exception;

    /**
     * 责任链中权限操作的删除行为
     *
     * @param id 权限id
     */
    abstract public void permDeleteBehavior(String id);

    /**
     * 责任链中权限查找的行为
     *
     * @param id 权限id
     * @return 查找出的权限信息
     */
    abstract public Object permFindBehavior(String id);
}
