package com.hrm.salary.dao;


import com.hrm.domain.salary.SalaryArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 自定义dao接口继承
 * JpaRepository<实体类，主键>
 * JpaSpecificationExecutor<实体类>
 */
public interface SalaryArchiveDao extends JpaRepository<SalaryArchive, String>, JpaSpecificationExecutor<SalaryArchive> {
    /**
     * 根据企业id和归档月份查询薪资归档数据，某月的
     *
     * @param companyId
     * @param yearMonth
     * @return
     */
    SalaryArchive findByCompanyIdAndYearsMonth(String companyId, String yearMonth);

    /**
     * 查询某一年的企业薪资归档主档数据
     *
     * @param companyId
     * @param yearMonth
     * @return
     */
    List<SalaryArchive> findByCompanyIdAndYearsMonthLike(String companyId, String yearMonth);
}
