package com.hrm.domain.attendance.entity;


import com.hrm.domain.attendance.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 调休配置表
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "atte_day_off_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayOffConfig extends BaseEntity implements Serializable {

    @Id
    private String id;
    private String companyId;

    /**
     * 部门ID
     */
    private String departmentId;


    /**
     * 调休过期月数
     */
    private String latestEffectDate;
    /**
     * 调休单位
     */
    private String unit;


}
