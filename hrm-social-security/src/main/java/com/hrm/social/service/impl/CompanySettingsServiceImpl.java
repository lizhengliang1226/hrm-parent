package com.hrm.social.service.impl;

import com.hrm.domain.social.CompanySettings;
import com.hrm.social.dao.CompanySettingsDao;
import com.hrm.social.service.CompanySettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CompanySettingsServiceImpl implements CompanySettingsService {
    @Autowired
    private CompanySettingsDao companySettingsDao;

    @Override
    public CompanySettings findById(String companyId) {
        Optional<CompanySettings> optional = companySettingsDao.findById(companyId);
        return optional.orElse(null);
    }

    @Override
    public void save(CompanySettings companySettings) {
        companySettings.setIsSettings(1);
        companySettingsDao.save(companySettings);
    }
}
