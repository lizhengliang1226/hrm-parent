package com.hrm.attendance.dao;


import com.hrm.domain.attendance.entity.ArchiveMonthly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 归档主档dao
 *
 * @author 17314
 */
public interface ArchiveMonthlyDao
        extends PagingAndSortingRepository<ArchiveMonthly, String>, CrudRepository<ArchiveMonthly, String>, JpaRepository<ArchiveMonthly, String>,
        JpaSpecificationExecutor<ArchiveMonthly> {


    /**
     * 查询某企业某一年的归档主列表
     *
     * @param companyId
     * @param archiveYear
     * @return
     */
    List<ArchiveMonthly> findByCompanyIdAndArchiveYear(String companyId, String archiveYear);

    /**
     * 查询主归档
     *
     * @param companyId
     * @param archiveYear
     * @param archiveMonth
     * @return
     */
    List<ArchiveMonthly> findByCompanyIdAndArchiveYearAndArchiveMonth(String companyId, String archiveYear, String archiveMonth);

    /**
     * 查询归档主档
     *
     * @param companyId
     * @param year
     * @param departmentId
     * @return
     */
    List<ArchiveMonthly> findByCompanyIdAndArchiveYearAndDepartmentId(String companyId, String year, String departmentId);
}
