package com.hrm.social.service;


import com.hrm.domain.social.CompanySettings;

public interface CompanySettingsService {


	/**
	 * 根据企业id查询社保设置
	 *
	 * @param companyId 企业id
	 * @return 企业设置
	 */
	public CompanySettings findById(String companyId);

	/**
	 * 保存企业设置
	 *
	 * @param companySettings 企业社保设置
	 */
	public void save(CompanySettings companySettings);
}
