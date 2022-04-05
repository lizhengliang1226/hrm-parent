package com.hrm.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 基本dao
 *
 * @author LZL
 * @version v1.0
 * @date 2022/4/1-14:40
 */
public interface BaseDao<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
