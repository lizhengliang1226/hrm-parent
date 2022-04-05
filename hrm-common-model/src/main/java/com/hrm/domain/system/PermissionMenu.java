package com.hrm.domain.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Description: 菜单权限实体类
 *
 * @author 17314
 */
@Entity
@Table(name = "pe_permission_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SelectBeforeUpdate
@DynamicInsert
@DynamicUpdate
@ApiModel("菜单权限实体类")
public class PermissionMenu implements Serializable {
    private static final long serialVersionUID = -1002411490113957485L;

    @Id
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("图标")
    private String menuIcon;

    @ApiModelProperty("排序号")
    private String menuOrder;
}