package com.hrm.attendance.dao;


import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 归档明细dao
 *
 * @author 17314
 */
public interface ArchiveMonthlyInfoDao
        extends CrudRepository<AttendanceArchiveMonthlyInfo, String>, JpaRepository<AttendanceArchiveMonthlyInfo, String>,
        JpaSpecificationExecutor<AttendanceArchiveMonthlyInfo> {


    /**
     * 根据归档列表查询月归档详情
     *
     * @param atteArchiveMonthlyId
     * @return
     */
    List<AttendanceArchiveMonthlyInfo> findByAtteArchiveMonthlyId(String atteArchiveMonthlyId);

    /**
     * 根据父id删除多条归档记录
     *
     * @param id
     */
    @Modifying
    @Query(value = "delete from atte_archive_monthly_info where atte_archive_monthly_id=?1", nativeQuery = true)
    void deleteByAtteArchiveMonthlyId1(String id);

    /**
     * 根据userid和月份查询用户当月的考勤归档明细
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    AttendanceArchiveMonthlyInfo findByUserIdAndArchiveDate(String userId, String yearMonth);

}