package com.hrm.domain.attendance.vo;


import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 考勤数据导入模板
 *
 * @author 17314
 */
@Data
@ToString
public class AtteUploadVo {

	//员工姓名	手机号	工号	上班时间	下班时间 考勤日期
	@ExcelProperty(order = 0)
	private String username;
	@ExcelProperty(order = 1)
	private String mobile;
	@ExcelProperty(order = 2)
	private String workNumber;
	@ExcelProperty(order = 3)
	@DateTimeFormat(DatePattern.NORM_DATETIME_MINUTE_PATTERN)
	private Date inTime;
	@ExcelProperty(order = 4)
	@DateTimeFormat(DatePattern.NORM_DATETIME_MINUTE_PATTERN)
	private Date outTime;
	@ExcelProperty(order = 5)
	private String atteDate;
}
