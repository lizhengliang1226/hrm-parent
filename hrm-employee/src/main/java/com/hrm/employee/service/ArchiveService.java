package com.hrm.employee.service;

import com.hrm.domain.employee.EmployeeArchive;

import java.util.List;

/**
 * 月度员工归档服务
 *
 * @author LZL
 * @date 2022/3/15-0:57
 */
public interface ArchiveService {
    /**
     * 保存月度归档信息
     *
     * @param archive
     */
    public void save(EmployeeArchive archive);

    /**
     * 查找
     *
     * @param companyId
     * @param month
     * @return
     */
    public EmployeeArchive findLast(String companyId, String month);

    /**
     * 查询全部归档信息
     *
     * @param page
     * @param pagesize
     * @param year
     * @param companyId
     * @return
     */
    public List<EmployeeArchive> findAll(Integer page, Integer pagesize, String year, String companyId);

    /**
     * 查询某年的归档数量
     *
     * @param year
     * @param companyId
     * @return
     */
    public Long countAll(String year, String companyId);

    /**
     * 分页查询月度归档信息
     *
     * @return
     */
    public List<EmployeeArchive> findSearch(String year, String companyId);

    /**
     * 根据月份查询归档主档信息
     *
     * @param month1
     * @return
     */
    EmployeeArchive findByMonth(String month1);
}
