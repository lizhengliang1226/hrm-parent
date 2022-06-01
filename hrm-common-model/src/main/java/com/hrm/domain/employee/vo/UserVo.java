package com.hrm.domain.employee.vo;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author LZL
 * @version v1.0
 * @date 2022/5/20-0:43
 */
@Data
public class UserVo {
    @ApiModelProperty("用户名称")
    @ExcelProperty(value = "用户名")
    private String username;
    @ApiModelProperty("手机号码")
    @ExcelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty("工号")
    @ExcelProperty(value = "工号")
    private String workNumber;
    @ApiModelProperty("聘用形式")
    @ExcelProperty(value = "聘用形式")
    private Integer formOfEmployment;


    @ApiModelProperty("入职时间")
    @ExcelProperty(value = "入职时间")
    @DateTimeFormat(DatePattern.NORM_DATE_PATTERN)
    private Date timeOfEntry;

    @ApiModelProperty("部门ID")
    @ExcelProperty(value = "部门编码")
    private String departmentCode;


    @ApiModelProperty("管理形式")
    @ExcelProperty(value = "管理形式")
    private String formOfManagement;

    @ApiModelProperty("工作城市")
    @ExcelProperty(value = "工作城市")
    private String workingCity;

}
