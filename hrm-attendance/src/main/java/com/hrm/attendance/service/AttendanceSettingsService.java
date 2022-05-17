package com.hrm.attendance.service;

import com.hrm.domain.attendance.entity.AttendanceConfig;
import com.hrm.domain.attendance.entity.DeductionDict;
import com.hrm.domain.attendance.entity.LeaveConfig;
import com.hrm.domain.attendance.vo.ExtDutyVO;
import com.hrm.domain.attendance.vo.ExtWorkVO;

import java.util.List;


/**
 * 考勤设置服务
 *
 * @author 17314
 */
public interface AttendanceSettingsService {


    /**
     * 根据部门id查询部门考勤设置
     *
     * @param companyId
     * @param departmentId
     * @return
     */
    public AttendanceConfig getAtteConfig(String companyId, String departmentId);

    /**
     * 保存或更新考勤设置
     *
     * @param attendanceConfig
     */
    public void saveAtteConfig(AttendanceConfig attendanceConfig);

    /**
     * 根据部门id查询部门请假设置
     *
     * @param companyId
     * @param departmentId
     * @return
     */
    public List<LeaveConfig> getLeaveConfig(String companyId, String departmentId);

    /**
     * 保存或更新请假设置
     *
     * @param leaveConfig
     */
    public void saveLeaveConfig(List<LeaveConfig> leaveConfig);

    /**
     * 根据部门id查询部门扣款设置
     *
     * @param companyId
     * @param departmentId
     * @return
     */
    public List<DeductionDict> getDeductionConfig(String companyId, String departmentId);

    /**
     * 保存或更新扣款设置
     *
     * @param leaveConfig
     */
    public void saveDeductionConfig(List<DeductionDict> leaveConfig);

    /**
     * 获取加班配置
     *
     * @param companyId
     * @param departmentId
     * @return
     */
    ExtWorkVO getExtraWorkConfig(String companyId, String departmentId);

    /**
     * 保存或更新加班配置
     *
     * @param edv
     */
    void saveExtraWorkSet(ExtDutyVO edv);
}
