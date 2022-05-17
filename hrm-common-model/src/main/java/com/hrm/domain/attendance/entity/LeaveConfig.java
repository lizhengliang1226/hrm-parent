package com.hrm.domain.attendance.entity;


import com.hrm.domain.attendance.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 请假配置表
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "atte_leave_config")
@Data
@NoArgsConstructor
public class LeaveConfig extends BaseEntity implements Serializable {

    @Id
    private String id;

    private String companyId;

    private String departmentId;

    private String leaveType; //类型

    private Integer isEnable;

    public LeaveConfig(String id, String companyId, String departmentId, String leaveType, Integer isEnable) {
        this.id = id;
        this.companyId = companyId;
        this.departmentId = departmentId;
        this.leaveType = leaveType;
        this.isEnable = isEnable;
    }
}
