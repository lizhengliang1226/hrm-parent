package com.hrm.social.service;


import com.hrm.common.entity.PageResult;
import com.hrm.domain.social.UserSocialSecurity;
import com.hrm.domain.social.vo.UserSocialSecuritySimpleVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UserSocialService {


	/**
	 * 分页查询用户的社保数据
	 *
	 * @return 分页数据
	 */
	public PageResult<UserSocialSecuritySimpleVo> findAll(Map map);

	/**
	 * 根据id查询用户社保信息
	 *
	 * @param id
	 * @return
	 */
	public UserSocialSecurity findById(String id);

	public void save(UserSocialSecurity uss);

	/**
	 * 批量导入社保数据
	 *
	 * @param file
	 * @param companyId
	 */
	void importSocialExcel(MultipartFile file, String companyId) throws IOException;
}
