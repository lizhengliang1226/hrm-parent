package com.hrm.domain.salary.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSalaryItemVo implements Serializable {
    private static final long serialVersionUID = 3133921121701427933L;
    //ID
    private String id;

    /**
     * 姓名
     */
    private String username;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 工号
     */
    private String workNumber;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 聘用形式
     */
    private String formOfEmployment;
    /**
     * 入职时间
     */
    private String timeOfEntry;
    /**
     * 工资基数
     */
    private BigDecimal wageBase;
    /**
     * 津贴方案
     */
    private Integer subsidyScheme;
    /**
     * 是否定薪
     */
    private Boolean isFixed;
}
