package com.hrm.common.service;

import com.hrm.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础服务实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/4/1-14:45
 */
public class BaseServiceImpl<M extends BaseDao<T, ID>, T, ID> extends BaseSpecService<T> implements BaseService<T, ID> {
    @Autowired
    private M dao;

    @Override
    public void deleteById(ID id) {
        dao.deleteById(id);
    }

    @Override
    public T findById(ID id) {
        return dao.findById(id).get();
    }
}
