package com.hrm.company.service;

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
     * @return 全部企业列表
     */
    public List<Company> findAll();
}
