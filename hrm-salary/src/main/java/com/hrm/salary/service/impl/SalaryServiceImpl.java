package com.hrm.salary.service.impl;

import com.alibaba.fastjson.JSON;
import com.hrm.common.client.SocialSecurityClient;
import com.hrm.common.entity.PageResult;
import com.hrm.domain.salary.UserSalary;
import com.hrm.domain.salary.vo.SalaryItemVo;
import com.hrm.domain.social.UserSocialSecurity;
import com.hrm.salary.dao.UserSalaryDao;
import com.hrm.salary.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SalaryServiceImpl implements SalaryService {

    @Autowired
    private UserSalaryDao userSalaryDao;
    @Autowired
    private SocialSecurityClient socialSecurityClient;

    //定薪或者调薪
    @Override
    public void saveUserSalary(UserSalary userSalary) {
        final String userId = userSalary.getUserId();
        final Map<String, Object> data = socialSecurityClient.findUserSocialInfo(userId).getData();
        final Object userSocialSecurity = data.get("userSocialSecurity");
        final UserSocialSecurity u = JSON.parseObject(
                JSON.toJSONString(userSocialSecurity),
                UserSocialSecurity.class);
        final BigDecimal base = userSalary.getCurrentBasicSalary().add(userSalary.getCurrentPostWage());
        u.setProvidentFundBase(base);
        u.setSocialSecurityBase(base);
        socialSecurityClient.saveUserSocialInfo(u);
        userSalaryDao.save(userSalary);
    }


    @Override
    public UserSalary findUserSalary(String userId) {
        Optional<UserSalary> optional = userSalaryDao.findById(userId);
        return optional.orElse(null);
    }

    //分页查询当月薪资列表
    @Override
    public PageResult<SalaryItemVo> findAll(Integer page, Integer pageSize, String companyId) {
        Page<Map> page1 = userSalaryDao.findPage(companyId, PageRequest.of(page - 1, pageSize));
        final List content = page1.getContent();
        return new PageResult(page1.getTotalElements(), content);
    }

    @Override
    public Map findUserSalaryDetail(String userId, String yearMonth) {
        Map userSalaryDetailInfo = userSalaryDao.findUserSalaryDetailInfo(userId, yearMonth);
        return userSalaryDetailInfo;
    }

}
