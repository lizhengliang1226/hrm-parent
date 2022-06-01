package com.hrm.social.dao;

import com.hrm.domain.social.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 自定义dao接口继承
 * JpaRepository<实体类，主键>
 * JpaSpecificationExecutor<实体类>
 */
public interface ArchiveDao extends JpaRepository<Archive, String>, JpaSpecificationExecutor<Archive> {
    /**
     * 查询归档信息通过企业id和年月
     *
     * @param companyId
     * @param yearsMonth
     * @return
     */
    public Archive findByCompanyIdAndYearsMonth(String companyId, String yearsMonth);

    /**
     * 根据企业id和年份查询某年的归档主档列表
     *
     * @param companyId
     * @param year
     * @return
     */
    List<Archive> findByCompanyIdAndYearsMonthLike(String companyId, String year);
}
