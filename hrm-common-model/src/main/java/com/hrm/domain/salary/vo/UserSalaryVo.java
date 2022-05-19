package com.hrm.domain.salary.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSalaryVo implements Serializable {
    private static final long serialVersionUID = -4263180665840088999L;
    //ID
    private String id;

    /**
     * 姓名
     */
    private String username;
    /**
     * 电话
     */
    private String mobile;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 工号
     */
    private String workNumber;
    /**
     * 聘用形式
     */
    private Integer formOfEmployment;

    /**
     * 入职时间
     */
    private Date timeOfEntry;
    /**
     * 工资基数
     */
    private BigDecimal wageBase;

    /**
     * 是否定薪
     */
    private BigInteger isFixed;
    private Integer inServiceStatus;

}
