package com.hrm.salary.service;


import com.hrm.common.entity.PageResult;
import com.hrm.domain.salary.UserSalary;
import com.hrm.domain.salary.vo.SalaryItemVo;

import java.util.Map;


public interface SalaryService {


    /**
     * 定薪或调薪
     *
     * @param userSalary
     */
    public void saveUserSalary(UserSalary userSalary);

    /**
     * 查询用户的薪资
     *
     * @param userId
     * @return
     */
    public UserSalary findUserSalary(String userId);

    /**
     * 分页查询薪资列表
     * @param map
     * @return
     */
    public PageResult<SalaryItemVo> findAll(Map map);

    /**
     * 查询某月用户薪资详情
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    Map findUserSalaryDetail(String userId, String yearMonth);
}
