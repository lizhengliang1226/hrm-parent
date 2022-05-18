package com.hrm.attendance.dao;


import com.hrm.domain.attendance.entity.AttendanceCompanySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * 考勤月份企业设置dao
 *
 * @author 17314
 */
public interface AttendanceCompanySettingsDao
        extends JpaRepository<AttendanceCompanySettings, String>, JpaSpecificationExecutor<AttendanceCompanySettings> {

}
