package com.hrm.social.dao;


import com.hrm.domain.social.PaymentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 自定义dao接口继承
 * JpaRepository<实体类，主键>
 * JpaSpecificationExecutor<实体类>
 */
public interface PaymentItemDao extends JpaRepository<PaymentItem, String>, JpaSpecificationExecutor<PaymentItem> {

}
