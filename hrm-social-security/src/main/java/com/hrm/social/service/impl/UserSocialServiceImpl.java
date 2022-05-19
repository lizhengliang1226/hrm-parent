package com.hrm.social.service.impl;

import com.hrm.domain.social.CityPaymentItem;
import com.hrm.domain.social.UserSocialSecurity;
import com.hrm.social.dao.CityPaymentItemDao;
import com.hrm.social.dao.UserSocialSecurityDao;
import com.hrm.social.service.UserSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 企业社保缴纳设置实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/15-9:55
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserSocialServiceImpl implements UserSocialService {

    @Autowired
    private UserSocialSecurityDao userSocialSecurityDao;
    @Autowired
    private CityPaymentItemDao cityPaymentItemDao;

    @Override
    public Page<Map> findAll(Integer page, Integer pageSize, String companyId) {
        final Page<Map> page1 = userSocialSecurityDao.findPage(companyId, PageRequest.of(page - 1, pageSize));
        return page1;
    }

    @Override
    public UserSocialSecurity findById(String id) {
        Optional<UserSocialSecurity> optional = userSocialSecurityDao.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void save(UserSocialSecurity uss) {
        final List<CityPaymentItem> byCityId = cityPaymentItemDao.findByCityId(uss.getParticipatingInTheCityId());
        BigDecimal comTol = new BigDecimal(0);
        BigDecimal personalTol = new BigDecimal(0);
        DecimalFormat df = new DecimalFormat("#.00");
        // 计算企业和个人缴纳的社保费用
        for (CityPaymentItem item : byCityId) {
            if ("工伤".equals(item.getName()) && item.getSwitchCompany()) {
                item.setScaleCompany(uss.getIndustrialInjuryRatio());
            }
            if (item.getSwitchCompany()) {
                final BigDecimal divide = uss.getSocialSecurityBase().multiply(item.getScaleCompany()).divide(new BigDecimal(100));
                final String format = df.format(divide);
                final BigDecimal bigDecimal = new BigDecimal(format);
                comTol = comTol.add(bigDecimal);
            }
            if (item.getSwitchPersonal()) {
                final BigDecimal divide = uss.getSocialSecurityBase().multiply(item.getScalePersonal()).divide(new BigDecimal(100));
                final String format = df.format(divide);
                final BigDecimal bigDecimal = new BigDecimal(format);
                comTol = comTol.add(bigDecimal);
            }
        }
        // 计算公积金费用
        final BigDecimal divide = uss.getEnterpriseProportion().multiply(uss.getProvidentFundBase()).divide(new BigDecimal(100));
        final String format = df.format(divide);
        final BigDecimal com = new BigDecimal(format);
        final BigDecimal divide1 = uss.getPersonalProvidentFundPayment().multiply(uss.getProvidentFundBase()).divide(new BigDecimal(100));
        final String format1 = df.format(divide);
        final BigDecimal person = new BigDecimal(format);
        uss.setEnterpriseProvidentFundPayment(com);
        uss.setPersonalProvidentFundPayment(person);
        uss.setSocialSecurityCompanyBase(comTol);
        uss.setSocialSecurityPersonalBase(personalTol);
        uss.setLastModifyTime(new Date());
        uss.setSocialSecuritySwitchUpdateTime(new Date());
        uss.setProvidentFundSwitchUpdateTime(new Date());
        userSocialSecurityDao.save(uss);
    }
}
