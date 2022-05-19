package com.hrm.attendance.service;


import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.attendance.vo.ArchiveMonthlyVO;

import java.util.List;

public interface ArchiveService {

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
	 * @param departmentId
	 * @param year
	 * @return
	 */
	List<ArchiveMonthlyVO> findAtteHistoryData(String departmentId, String year, String companyId);

    /**
     * 根据主档id查询所有的子档
     *
     * @param id
     * @return
     */
    List<AttendanceArchiveMonthlyInfo> findAtteHistoryDetailData(String id);

    /**
     * 根据id和年份查询归档明细
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    AttendanceArchiveMonthlyInfo findUserMonthlyDetail(String userId, String yearMonth);
}
