package com.hrm.domain.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 *
 * @author 17314
 */
@Entity
@Table(name = "bs_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert()
@DynamicUpdate()
@ApiModel("用户实体类")
public class User implements Serializable {
    private static final long serialVersionUID = 4297464181093070302L;

    @Id
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("密码")
    private String password;


    @ApiModelProperty("启用状态 0为禁用 1为启用")
    private Integer enableState;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("企业ID")
    private String companyId;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("部门ID")
    private String departmentId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("入职时间")
    private Date timeOfEntry;

    @ApiModelProperty("离职时间")
    private Date timeOfDimission;

    @ApiModelProperty("聘用形式")
    private Integer formOfEmployment;

    @ApiModelProperty("工号")
    private String workNumber;

    @ApiModelProperty("管理形式")
    private String formOfManagement;

    @ApiModelProperty("工作城市")
    private String workingCity;

    @ApiModelProperty("转正时间")
    private Date correctionTime;

    @ApiModelProperty("在职状态 1.在职  2.离职")
    private Integer inServiceStatus;

    @ApiModelProperty("用户级别 saasAdmin,coAdmin,user")
    private String level;

    @ApiModelProperty("员工照片")
    private String staffPhoto;

    @ManyToMany()
    @JsonIgnore
    @JoinTable(name = "pe_user_role", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @ApiModelProperty("用户包含的角色-多对多")
    private Set<Role> roles = new HashSet<Role>();
}
