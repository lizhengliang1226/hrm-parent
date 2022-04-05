package com.hrm.common.service;

/**
 * 基本service接口
 *
 * @author LZL
 * @version v1.0
 * @date 2022/4/1-14:41
 */
public interface BaseService<T, ID> {
    /**
     * 根据id删除
     *
     * @param id id
     */
    public void deleteById(ID id);

    /**
     * 根据id查找
     *
     * @param id id
     * @return 对象实体
     */
    public T findById(ID id);
}
