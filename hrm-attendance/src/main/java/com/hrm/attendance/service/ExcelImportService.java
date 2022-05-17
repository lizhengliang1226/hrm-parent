package com.hrm.attendance.service;


import org.springframework.web.multipart.MultipartFile;


/**
 * @author 17314
 */
public interface ExcelImportService {


	/**
	 * 考勤数据导入
	 *
	 * @param file
	 * @param companyId
	 * @throws Exception
	 */
	public void importAttendanceExcel(MultipartFile file, String companyId) throws Exception;

}
