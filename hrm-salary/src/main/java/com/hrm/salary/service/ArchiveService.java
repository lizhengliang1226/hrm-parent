package com.hrm.salary.service;


import com.hrm.common.entity.PageResult;
import com.hrm.domain.salary.SalaryArchive;
import com.hrm.domain.salary.SalaryArchiveDetail;

import java.util.List;
import java.util.Map;

public interface ArchiveService {

    /**
     * 查询某月的薪资主档数据
     *
     * @param companyId
     * @param yearMonth
     * @return
     */
    SalaryArchive findSalaryArchive(String companyId, String yearMonth);

    /**
     * 查询某月薪资详细归档信息
     *
     * @param id
     * @return
     */
    PageResult<SalaryArchiveDetail> findSalaryArchiveDetail(String id, Map map);

    /**
     * 构建当月薪资报表
     *
     * @param yearMonth
     * @param companyId
     * @return
     */
    PageResult<SalaryArchiveDetail> getReports(String yearMonth, String companyId, int page, int pagesize) throws Exception;

    void saveArchive(String yearMonth, String companyId) throws Exception;

    void newReport(String yearMonth, String companyId);

    /**
     * 查询企业某一年的全部归档主档数据
     *
     * @param companyId
     * @param year
     * @return
     */
    List<SalaryArchive> findAllSalaryArchive(String companyId, String year);
}
