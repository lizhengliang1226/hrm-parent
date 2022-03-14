package com.hrm.domain.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 转正申请表
 * @author 17314
 */
@Entity
@Table(name = "em_positive")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("转正申请表")
public class EmployeePositive implements Serializable {
    private static final long serialVersionUID = 2391824518947910773L;

    @Id
    @ApiModelProperty("ID")
    private String userId;

    @ApiModelProperty("转正日期")
    private Date dateOfCorrection;

    @ApiModelProperty("转正评价")
    private String correctionEvaluation;

    @ApiModelProperty("附件")
    private String enclosure;

    @ApiModelProperty("单据状态 1是未执行，2是已执行")
    private Integer estatus;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
