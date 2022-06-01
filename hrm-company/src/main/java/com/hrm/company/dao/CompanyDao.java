package com.hrm.company.dao;

import com.hrm.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author LZL
 * @date 2022/1/12-10:02
 */
public interface CompanyDao extends JpaRepository<Company, String>, JpaSpecificationExecutor<Company> {
    /**
     * 根据管理者id查找企业
     *
     * @param id
     * @return
     */
    Company findByManagerId(String id);
}
