package com.hrm.social.service.impl;

import com.hrm.domain.social.CityPaymentItem;
import com.hrm.domain.social.PaymentItem;
import com.hrm.social.dao.CityPaymentItemDao;
import com.hrm.social.dao.PaymentItemDao;
import com.hrm.social.service.PaymentItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 企业社保缴纳设置实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/15-9:55
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PaymentItemServiceImpl implements PaymentItemService {

    @Autowired
    private PaymentItemDao paymentItemDao;

    @Autowired
    private CityPaymentItemDao cityPaymentItemDao;

    @Override
    public List<CityPaymentItem> findAllByCityId(String id) {
        final List<CityPaymentItem> byCityId = cityPaymentItemDao.findByCityId(id);
        return byCityId;
    }

    @Override
    public List<PaymentItem> findAllPaymentItems() {
        return paymentItemDao.findAll();
    }

    @Override
    public void saveCityPaymentItem(CityPaymentItem cityPaymentItem) {
        cityPaymentItemDao.save(cityPaymentItem);
    }
}
