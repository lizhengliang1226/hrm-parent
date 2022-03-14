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
 * 员工离职申请表
 * @author 17314
 */
@Entity
@Table(name = "em_resignation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("员工离职申请表")
public class EmployeeResignation implements Serializable {
    private static final long serialVersionUID = 2890789302883962744L;

    @Id
    @ApiModelProperty("ID")
    private String userId;

    @ApiModelProperty("离职时间")
    private String resignationTime;

    @ApiModelProperty("离职类型")
    private String typeOfTurnover;

    @ApiModelProperty("申请离职原因")
    private String reasonsForLeaving;

    @ApiModelProperty("补偿金")
    private String compensation;

    @ApiModelProperty("代通知金")
    private String notifications;

    @ApiModelProperty("社保减员月")
    private String socialSecurityReductionMonth;

    @ApiModelProperty("公积金减员月")
    private String providentFundReductionMonth;

    @ApiModelProperty("图片")
    private String picture;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
