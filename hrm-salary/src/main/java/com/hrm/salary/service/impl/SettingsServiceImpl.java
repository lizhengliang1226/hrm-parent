package com.hrm.salary.service.impl;

import com.hrm.domain.salary.Settings;
import com.hrm.salary.dao.SettingsDao;
import com.hrm.salary.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingsServiceImpl implements SettingsService {
    @Autowired
    private SettingsDao settingsDao;


    /**
     * 根据id获取企业设置
     *
     * @param companyId 企业id
     * @return 企业设置
     */
    @Override
    public Settings findById(String companyId) {
        Optional<Settings> optionalSettings = settingsDao.findById(companyId);
        return optionalSettings.isPresent() ? optionalSettings.get() : null;
    }

    /**
     * 保存配置
     */
    @Override
    public void save(Settings settings) {
        settingsDao.save(settings);
    }
}
