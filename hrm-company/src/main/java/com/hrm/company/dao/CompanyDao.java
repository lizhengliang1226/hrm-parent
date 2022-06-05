package com.hrm.company.dao;

import com.hrm.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    /**
     * 根据企业名称查询企业
     *
     * @param name
     * @return
     */
    List<Company> findByName(String name);

    @Query(value = "select * from co_company order by create_time desc limit ?1,?2 ", nativeQuery = true)
    List<Company> findPage(int page, int size);

    @Query(value = "select count(*) from co_company ", nativeQuery = true)
    int countOfFindPage();
}
