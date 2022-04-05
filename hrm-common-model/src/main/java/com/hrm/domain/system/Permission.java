package com.hrm.domain.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author 17314
 */
@Entity
@Table(name = "pe_permission")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SelectBeforeUpdate
@DynamicInsert
@DynamicUpdate
@EqualsAndHashCode
@ApiModel("权限实体类")
public class Permission implements Serializable {

    private static final long serialVersionUID = -4990810027542971546L;

    @Id
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限类型 1为菜单 2为功能 3为API")
    private Integer type;

    @ApiModelProperty("权限码")
    private String code;

    @ApiModelProperty("权限描述")
    private String description;

    @ApiModelProperty("父权限ID")
    private String pid;

    @ApiModelProperty("企业可见性 0：不可见，1：可见")
    private Integer enVisible;

    public Permission(String name, Integer type, String code, String description) {
        this.name = name;
        this.type = type;
        this.code = code;
        this.description = description;
    }


}