package com.hrm.social.service;


import com.hrm.domain.social.UserSocialSecurity;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UserSocialService {


	/**
	 * 分页查询用户的社保数据
	 *
	 * @param page      页数
	 * @param pageSize  页大小
	 * @param companyId 企业id
	 * @return 分页数据
	 */
	public Page<Map> findAll(Integer page, Integer pageSize, String companyId);

	/**
	 * 根据id查询用户社保信息
	 *
	 * @param id
	 * @return
	 */
	public UserSocialSecurity findById(String id);

	public void save(UserSocialSecurity uss);
}
