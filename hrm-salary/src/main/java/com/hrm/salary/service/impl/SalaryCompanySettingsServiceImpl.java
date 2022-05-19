package com.hrm.salary.service.impl;

import com.hrm.domain.salary.SalaryCompanySettings;
import com.hrm.salary.dao.SalaryCompanySettingsDao;
import com.hrm.salary.service.SalaryCompanySettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SalaryCompanySettingsServiceImpl implements SalaryCompanySettingsService {

    @Autowired
    private SalaryCompanySettingsDao companySettingsDao;

    //根据id获取查询
    @Override
    public SalaryCompanySettings findById(String companyId) {
        Optional<SalaryCompanySettings> optionalCompanySettins = companySettingsDao.findById(companyId);
        return optionalCompanySettins.orElse(null);
    }

    //保存配置
    @Override
    public void save(SalaryCompanySettings companySettings) {
        companySettings.setIsSettings(1);
        companySettingsDao.save(companySettings);
    }
}
