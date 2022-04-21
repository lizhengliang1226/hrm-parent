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
import java.util.HashSet;
import java.util.Set;

/**
 * @author 17314
 */
@Entity
@Table(name = "pe_role")
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
//@EqualsAndHashCode
@ApiModel("角色实体类")
public class Role implements Serializable {
    private static final long serialVersionUID = 594829320797158219L;
    @Id
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("说明")
    private String description;

    @ApiModelProperty("企业id")
    private String companyId;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    @ApiModelProperty("角色包含的用户   多对多")
    private Set<User> users = new HashSet<User>(0);


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "pe_role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    @ApiModelProperty("角色包含的权限  多对多")
    private Set<Permission> permissions = new HashSet<Permission>(0);

}