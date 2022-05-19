package com.hrm.salary.service;


import com.hrm.domain.salary.SalaryCompanySettings;


public interface SalaryCompanySettingsService {


    //根据id获取查询
    public SalaryCompanySettings findById(String companyId);

    //保存配置
    public void save(SalaryCompanySettings companySettings);
}
