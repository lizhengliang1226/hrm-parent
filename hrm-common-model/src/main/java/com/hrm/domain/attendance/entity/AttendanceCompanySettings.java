package com.hrm.domain.attendance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 企业考勤月份设置表
 *
 * @author 17314
 */
@Entity
@Table(name = "atte_company_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceCompanySettings implements Serializable {
    private static final long serialVersionUID = 5753095342370704726L;
    /**
     * 企业id
     */
    @Id
    private String companyId;
    /**
     * 是否设置 0为未设置，1为已设置
     */
    private Integer isSettings;
    /**
     * 当前显示报表月份
     */
    private String dataMonth;
}
