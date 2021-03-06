package com.hrm.domain.attendance.entity;


import com.hrm.domain.attendance.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 加班配置表
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "atte_extra_duty_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class ExtraDutyConfig extends BaseEntity implements Serializable {

    @Id
    private String id;
    private String companyId;
    private String departmentId;

    /**
     * 每日标准工作时长，单位小时
     */
    private String workHoursDay;
    /**
     * 是否打卡0开启1关闭
     */
    private Integer isClock;
    /**
     * 是否开启加班补偿0开启1关闭
     */
    private Integer isCompensation;


}
