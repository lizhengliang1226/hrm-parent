package com.hrm.domain.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author 17314
 */
@Entity
@Table(name = "pe_permission_api")
@Data
@ApiModel("权限API实体类")
@AllArgsConstructor
@NoArgsConstructor
public class PermissionApi implements Serializable {

    private static final long serialVersionUID = -1803315043290784820L;

    @Id
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("链接")
    private String apiUrl;

    @ApiModelProperty("请求类型")
    private String apiMethod;

    @ApiModelProperty("权限等级，1为通用接口权限，2为需校验接口权限")
    private String apiLevel;
}