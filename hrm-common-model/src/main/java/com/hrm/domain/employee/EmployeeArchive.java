package com.hrm.domain.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 月度员工归档表
 * @author 17314
 */
@Entity
@Table(name = "em_archive")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("月度员工归档表")
public class EmployeeArchive implements Serializable {
    private static final long serialVersionUID = 5768915936056289957L;

    @Id
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("操作人")
    private String opUser;

    @ApiModelProperty("月份")
    private String month;

    @ApiModelProperty("企业ID")
    private String companyId;

    @ApiModelProperty("总人数")
    private Integer totals;

    @ApiModelProperty("在职人数")
    private Integer payrolls;

    @ApiModelProperty("离职人数")
    private Integer departures;

    @ApiModelProperty("数据")
    private String data;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
