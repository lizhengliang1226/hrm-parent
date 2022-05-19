package com.hrm.salary.service;


import com.hrm.domain.salary.SalaryArchive;
import com.hrm.domain.salary.SalaryArchiveDetail;

import java.util.List;

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
    List<SalaryArchiveDetail> findSalaryArchiveDetail(String id);

    /**
     * 构建当月薪资报表
     *
     * @param yearMonth
     * @param companyId
     * @return
     */
    List<SalaryArchiveDetail> getReports(String yearMonth, String companyId) throws Exception;

    void saveArchive(String yearMonth, String companyId) throws Exception;

    void newReport(String yearMonth, String companyId);
}
