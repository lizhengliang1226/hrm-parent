package com.hrm.attendance.service;


public interface ArchiveService {

	/**
	 * 考勤数据归档
	 *
	 * @param archiveDate
	 * @param companyId
	 */
	public void saveArchive(String archiveDate, String companyId);
}
