package com.hrm.attendance.service;


import com.hrm.common.entity.PageResult;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.attendance.vo.ArchiveMonthlyVO;

import java.util.List;
import java.util.Map;

/**
 * 考勤归档服务
 *
 * @author 17314
 */
public interface AtteArchiveService {

    /**
     * 考勤数据归档
     *
     * @param archiveDate
     * @param companyId
     */
    public void saveArchive(String archiveDate, String companyId);

    /**
     * 根据企业id和部门查询归档历史
     *
     * @param year
     * @return
     */
    List<ArchiveMonthlyVO> findAtteHistoryData(String year, String companyId);

    /**
     * 根据主档年月查询所有的子档
     *
     * @param id
     * @return
     */
    PageResult<AttendanceArchiveMonthlyInfo> findAtteHistoryDetailData(String id, Map map);

    /**
     * 根据id和年份查询归档明细
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    AttendanceArchiveMonthlyInfo findUserMonthlyDetail(String userId, String yearMonth);
}
