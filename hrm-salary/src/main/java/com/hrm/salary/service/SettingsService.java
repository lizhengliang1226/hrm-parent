package com.hrm.salary.service;


import com.hrm.domain.salary.Settings;
import org.springframework.stereotype.Service;

@Service
public interface SettingsService {


    /**
     * 根据id获取企业设置
     *
     * @param companyId 企业id
     * @return 企业设置
     */
    public Settings findById(String companyId);

    /**
     * 保存配置
     */
    public void save(Settings settings);
}
