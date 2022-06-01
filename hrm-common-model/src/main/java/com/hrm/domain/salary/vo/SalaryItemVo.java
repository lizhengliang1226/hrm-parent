package com.hrm.domain.salary.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 17314
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryItemVo implements Serializable {
    private static final long serialVersionUID = 2270963764329229065L;
    /**
     * id
     */
    private String id;
    /**
     * 用户名称
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 工号
     */
    private String workNumber;
    /**
     * 聘用形式
     */
    private String formOfEmployment;
    /**
     * 部门
     */
    private String department;
    /**
     * 入职时间
     */
    private Date timeOfEntry;
    /**
     * 工资基数
     */
    private BigDecimal wageBase;
    /**
     岗位薪资
     */
//    private BigDecimal currentPostWage;
    /* currentBasicSalary," +
					"sauss.current_post_wage currentPostWage,  */
    /**
     * 津贴方案
     */
//    private Integer subsidyScheme;
    private Integer inServiceStatus;
    /**
     * 是否定薪
     */
    private Integer isFixed;
}
