package com.hrm.social.service;


import com.hrm.domain.social.CityPaymentItem;
import com.hrm.domain.social.PaymentItem;

import java.util.List;

public interface PaymentItemService {

    /**
     * 根据城市id获取该城市的参保项
     *
     * @param id
     * @return
     */
    public List<CityPaymentItem> findAllByCityId(String id);

    /**
     * 查询五险的基础缴纳比例
     *
     * @return
     */
    List<PaymentItem> findAllPaymentItems();

    /**
     * 保存城市社保缴纳比例
     *
     * @param cityPaymentItem
     */
    void saveCityPaymentItem(CityPaymentItem cityPaymentItem);
}
