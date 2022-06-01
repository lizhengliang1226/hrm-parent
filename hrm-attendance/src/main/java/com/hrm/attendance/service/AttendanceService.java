package com.hrm.attendance.service;


import com.hrm.common.entity.PageResult;
import com.hrm.domain.attendance.entity.Attendance;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.attendance.entity.AttendanceCompanySettings;

import java.text.ParseException;
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
	public Map getAtteData(Map map) throws ParseException;

	/**
	 * 修改考勤记录
	 *
	 * @param attendance
	 */
	public void saveOrUpdateAtte(Attendance attendance);

	/**
	 * 查询考勤归档数据
	 *
	 * @param atteDate
	 * @param companyId
	 * @return
	 */
	public PageResult<AttendanceArchiveMonthlyInfo> getReports(String atteDate, String companyId, int page, int pagesize);

	/**
	 * 新建某年某月某家企业的考勤报表设置信息
	 *
	 * @param yearMonth
	 * @param companyId
	 */
	void newReport(String yearMonth, String companyId);

	/**
	 * 通过企业id查询企业考勤设置
	 *
	 * @param companyId
	 * @return
	 */
	AttendanceCompanySettings findMonthById(String companyId);

	/**
	 * 保存企业考勤设置
	 *
	 * @param companySettings
	 */
	void saveSetMonth(AttendanceCompanySettings companySettings);
}
