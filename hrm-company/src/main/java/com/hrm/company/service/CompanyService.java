package com.hrm.company.service;

import com.hrm.common.entity.PageResult;
import com.hrm.domain.company.Company;

import java.util.List;

/**
 * 企业服务
 *
 * @author LZL
 * @date 2022/1/12-10:17
 */
public interface CompanyService {
    /**
     * 保存企业
     * @param company 企业实体
     */
    public void add(Company company);

    /**
     * 删除企业
     * @param id 企业id
     */
    public void delete(String id);

    /**
     * 更新企业
     * @param company 企业实体
     */
    public void update(Company company);

    /**
     * 根据id查询企业
     * @param id 企业id
     * @return 企业实体
     */
    public Company findById(String id);

    /**
     * 查询企业列表
     *
     * @return 全部企业列表
     */
    public PageResult<Company> findAll(int page, int size);

    /**
     * 根据管理者id查询企业信息
     *
     * @param id
     * @return
     */
    Company findByManagerId(String id);

    /**
     * 根据公司名称查找公司
     *
     * @param name
     * @return
     */
    List<Company> findByName(String name);
}
