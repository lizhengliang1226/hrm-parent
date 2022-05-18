package com.hrm.attendance.service;


import com.hrm.domain.attendance.entity.ArchiveMonthlyInfo;
import com.hrm.domain.attendance.entity.Attendance;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface AttendanceService {


	/**
	 * 获取考勤数据
	 *
	 * @param companyId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ParseException
	 */
	public Map getAtteDate(String companyId, int page, int pageSize) throws ParseException;

	/**
	 * 修改考勤记录
	 *
	 * @param attendance
	 */
	public void editAtte(Attendance attendance);

    /**
     * 查询考勤归档数据
     *
     * @param atteDate
     * @param companyId
     * @return
     */
    public List<ArchiveMonthlyInfo> getReports(String atteDate, String companyId);

    /**
     * 新建某年某月某家企业的考勤报表设置信息
     *
     * @param yearMonth
     * @param companyId
     */
    void newReport(String yearMonth, String companyId);
}
