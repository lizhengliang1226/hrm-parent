package com.hrm.attendance.dao;


import com.hrm.domain.attendance.entity.ArchiveMonthlyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArchiveMonthlyInfoDao
        extends CrudRepository<ArchiveMonthlyInfo, String>, JpaRepository<ArchiveMonthlyInfo, String>, JpaSpecificationExecutor<ArchiveMonthlyInfo> {


    /**
     * 根据归档列表查询月归档详情
     *
     * @param atteArchiveMonthlyId
     * @return
     */
    List<ArchiveMonthlyInfo> findByAtteArchiveMonthlyId(String atteArchiveMonthlyId);

    /**
     * 根据父id删除多条归档记录
     *
     * @param id
     */
    void deleteByAtteArchiveMonthlyId(String id);

    /**
     * 根据userid和月份查询用户当月的考勤归档明细
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    ArchiveMonthlyInfo findByUserIdAndArchiveDate(String userId, String yearMonth);
}