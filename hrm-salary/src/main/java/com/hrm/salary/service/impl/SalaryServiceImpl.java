package com.hrm.salary.service.impl;

import com.hrm.common.client.SocialSecurityClient;
import com.hrm.common.entity.PageResult;
import com.hrm.common.utils.PageUtils;
import com.hrm.domain.salary.UserSalary;
import com.hrm.domain.salary.vo.SalaryItemVo;
import com.hrm.salary.dao.UserSalaryDao;
import com.hrm.salary.mapper.SalaryMapper;
import com.hrm.salary.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private SalaryMapper salaryMapper;

    //定薪或者调薪
    @Override
    public void saveUserSalary(UserSalary userSalary) {
        final String userId = userSalary.getUserId();
//        final Map<String, Object> data = socialSecurityClient.findUserSocialInfo(userId).getData();
//        final Object userSocialSecurity = data.get("userSocialSecurity");
//        final UserSocialSecurity u = JSON.parseObject(
//                JSON.toJSONString(userSocialSecurity),
//                UserSocialSecurity.class);
//        final BigDecimal base = userSalary.getCurrentBasicSalary().add(userSalary.getCurrentPostWage());
//        u.setProvidentFundBase(base);
//        u.setSocialSecurityBase(base);
//        socialSecurityClient.saveUserSocialInfo(u);
        userSalaryDao.save(userSalary);
    }


    @Override
    public UserSalary findUserSalary(String userId) {
        Optional<UserSalary> optional = userSalaryDao.findById(userId);
        return optional.orElse(new UserSalary());
    }


    @Override
    public PageResult<SalaryItemVo> findAll(Map map) {
        PageUtils.doPage(map);
        final List<SalaryItemVo> salaryList = salaryMapper.findSalaryList(map);
        final Integer integer = salaryMapper.countOfSalaryList(map);
        return new PageResult(Long.valueOf(integer), salaryList);
    }

    @Override
    public Map findUserSalaryDetail(String userId, String yearMonth) {
        Map userSalaryDetailInfo = userSalaryDao.findUserSalaryDetailInfo(userId, yearMonth);
        return userSalaryDetailInfo;
    }

}
