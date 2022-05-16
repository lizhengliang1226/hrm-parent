package com.hrm.social.service.impl;

import com.hrm.domain.social.UserSocialSecurity;
import com.hrm.social.dao.UserSocialSecurityDao;
import com.hrm.social.service.UserSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        uss.setLastModifyTime(new Date());
        uss.setSocialSecuritySwitchUpdateTime(new Date());
        uss.setProvidentFundSwitchUpdateTime(new Date());
        userSocialSecurityDao.save(uss);
    }
}
