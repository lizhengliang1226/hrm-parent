package com.hrm.salary.dao;


import com.hrm.domain.salary.SalaryArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 自定义dao接口继承
 * JpaRepository<实体类，主键>
 * JpaSpecificationExecutor<实体类>
 */
public interface ArchiveDao extends JpaRepository<SalaryArchive, String>, JpaSpecificationExecutor<SalaryArchive> {
    /**
     * 根据企业id和归档月份查询薪资归档数据
     *
     * @param companyId
     * @param yearMonth
     * @return
     */
    SalaryArchive findByCompanyIdAndYearsMonth(String companyId, String yearMonth);
}
