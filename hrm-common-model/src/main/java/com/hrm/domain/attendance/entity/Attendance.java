package com.hrm.domain.attendance.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.hrm.domain.attendance.base.BaseEntity;
import com.hrm.domain.attendance.vo.AtteUploadVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 考勤表
 */
@TableName("atte_attendance")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "atte_attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 594829320797158219L;

    @Id
    private String id;
    private String companyId;
    private String departmentId;

    private String userId;
    private Integer adtStatus;
    private long jobStatus;

    private Date adtInTime;
    private String adtInPlace;
    private String adtInHourse;

    private String adtInCoordinate;
    private Date adtOutTime;
    private String adtOutPlace;
    private String adtOutHourse;
    private String day;

    public Attendance(AtteUploadVo vo, User user) {
        this.adtInTime = vo.getInTime();
        this.adtOutTime = vo.getOutTime();
        this.userId = user.getId();
        this.companyId = user.getCompanyId();
        this.departmentId = user.getDepartmentId();
        this.jobStatus = user.getInServiceStatus();
    }

}
