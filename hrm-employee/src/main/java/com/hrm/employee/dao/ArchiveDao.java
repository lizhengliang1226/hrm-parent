package com.hrm.employee.dao;


import com.hrm.domain.employee.EmployeeArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 *
 * @author 17314
 */
public interface ArchiveDao extends JpaRepository<EmployeeArchive, String>, JpaSpecificationExecutor<EmployeeArchive> {
    /**
     * 查询某个月的归档报表
     *
     * @param companyId 企业id
     * @param month     月份
     * @return 月度报表
     */
    @Query(value = "SELECT * FROM em_archive WHERE company_id = ?1 AND month = ?2 ORDER BY create_time DESC LIMIT 1;", nativeQuery = true)
    EmployeeArchive findByLast(String companyId, String month);

    /**
     * 查询某一年的月度归档报表
     *
     * @param companyId 企业id
     * @param year      年份
     * @param index     开始
     * @param pageSize  数据量
     * @return 月度报表
     */
    @Query(value = "SELECT * FROM em_archive WHERE company_id = ?1 AND month LIKE ?2 GROUP BY month HAVING MAX(create_time) limit ?3,?4", nativeQuery = true)
    List<EmployeeArchive> findAllData(String companyId, String year, Integer index, Integer pageSize);

    /**
     * 查询月度归档报表的记录数
     *
     * @param companyId 企业id
     * @param year      年份
     * @return 记录数
     */
    @Query(value = "SELECT count(DISTINCT month) FROM em_archive WHERE company_id = ?1 AND month LIKE ?2", nativeQuery = true)
    long countAllData(String companyId, String year);

    /**
     * 根据年份查询出某一年所有月份的归档主表信息
     *
     * @param companyId
     * @param year
     * @return
     */
    @Query(value = "  select * " +
            "    from\n" +
            "        em_archive employeear0_ \n" +
            "    where\n" +
            "        employeear0_.company_id=?1 \n" +
            "        and " +
            "            employeear0_.month like ?2\n" +
            "       ", nativeQuery = true)
    List<EmployeeArchive> findByCompanyIdAndMonthLike(String companyId, String year);

    /**
     * 根据月份查询主档信息
     *
     * @param month1
     * @return
     */
    EmployeeArchive findByMonth(String month1);
}