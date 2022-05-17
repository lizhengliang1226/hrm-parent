package com.hrm.attendance.service.impl;

import com.hrm.attendance.dao.*;
import com.hrm.attendance.service.AttendanceSettingsService;
import com.hrm.domain.attendance.entity.*;
import com.hrm.domain.attendance.vo.ExtDutyVO;
import com.hrm.domain.attendance.vo.ExtWorkVO;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 考勤设置服务实现类
 *
 * @author 17314
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AttendanceSettingsServiceImpl implements AttendanceSettingsService {

    @Autowired
    private AttendanceConfigDao attendanceConfigDao;

    @Autowired
    private LeaveConfigDao leaveConfigDao;

    @Autowired
    private DeductionDictDao deductionDictDao;

    @Autowired
    private ExtraDutyConfigDao extraDutyConfigDao;

    @Autowired
    private ExtraDutyRuleDao extraDutyRuleDao;

    @Autowired
    private DayOffConfigDao dayOffConfigDao;

    @Override
    public AttendanceConfig getAtteConfig(String companyId, String departmentId) {
        //		return attendanceConfig == null ?new AttendanceConfig() :attendanceConfig;
        return attendanceConfigDao.findByCompanyIdAndDepartmentId(companyId, departmentId);
    }

    @Override
    public void saveAtteConfig(AttendanceConfig attendanceConfig) {
        //1.查询是否存在响应的考勤记录
        AttendanceConfig attendanceConfig1 = attendanceConfigDao.findByCompanyIdAndDepartmentId(attendanceConfig.getCompanyId(),
                                                                                                attendanceConfig.getDepartmentId());
        //2.如果存在,更新
        if (attendanceConfig1 != null) {
            attendanceConfig.setId(attendanceConfig1.getId());
            attendanceConfig.setUpdateDate(new Date());
        } else {
            //3.如果不存在,设置id,保存
            attendanceConfig.setId(IdWorker.getIdStr());
            attendanceConfig.setCreateDate(new Date());
            attendanceConfig.setUpdateDate(new Date());
        }
        attendanceConfigDao.save(attendanceConfig);
    }

    @Override
    public List<LeaveConfig> getLeaveConfig(String companyId, String departmentId) {
        return leaveConfigDao.findByCompanyIdAndDepartmentId(companyId, departmentId);
    }

    @Override
    public void saveLeaveConfig(List<LeaveConfig> leaveConfig) {
        leaveConfig.forEach(l -> {
            final LeaveConfig leave = leaveConfigDao.
                                                            findByCompanyIdAndDepartmentIdAndLeaveType(
                                                                    l.getCompanyId()
                                                                    , l.getDepartmentId(),
                                                                    l.getLeaveType());
            if (leave != null) {
                l.setId(leave.getId());
                l.setUpdateDate(new Date());
            } else {
                l.setCreateDate(new Date());
                l.setUpdateDate(new Date());
            }
        });
        leaveConfigDao.saveAll(leaveConfig);
    }

    @Override
    public List<DeductionDict> getDeductionConfig(String companyId, String departmentId) {
        return deductionDictDao.findByCompanyIdAndDepartmentId(companyId, departmentId);
    }

    @Override
    public void saveDeductionConfig(List<DeductionDict> deductionsList) {
        deductionsList.forEach(d -> {
            final DeductionDict leave = deductionDictDao.findByCompanyIdAndDepartmentIdAndDedTypeCode(d.getCompanyId(), d.getDepartmentId(),
                                                                                                      d.getDedTypeCode());
            if (leave != null) {
                d.setId(leave.getId());
                d.setUpdateDate(new Date());
            } else {
                d.setCreateDate(new Date());
                d.setUpdateDate(new Date());
            }
        });
        deductionDictDao.saveAll(deductionsList);
    }

    @Override
    public ExtWorkVO getExtraWorkConfig(String companyId, String departmentId) {
        ExtWorkVO ew = new ExtWorkVO();
        // 获取加班基础配置
        final ExtraDutyConfig edc = extraDutyConfigDao.findByCompanyIdAndDepartmentId(companyId, departmentId);
        if (edc == null) {
            ExtWorkVO ee = new ExtWorkVO();
            ee.setExtraDutyRuleList(new ArrayList<>());
            ee.setExtraDutyConfig(new ExtraDutyConfig());
            ee.setDayOffConfigs(new DayOffConfig());
            return ee;
        }
        final String id = edc.getId();
        // 获取加班规则配置
        final List<ExtraDutyRule> edrList = extraDutyRuleDao.findByExtraDutyConfigId(id);
        // 获取调休配置
        final DayOffConfig doc = dayOffConfigDao.findByCompanyIdAndDepartmentId(companyId, departmentId);
        ew.setDayOffConfigs(doc);
        ew.setExtraDutyConfig(edc);
        ew.setExtraDutyRuleList(edrList);
        return ew;
    }

    @Override
    public void saveExtraWorkSet(ExtDutyVO edv) {
        final String companyId = edv.getCompanyId();
        final String departmentId = edv.getDepartmentId();
        final ExtraDutyConfig edc = extraDutyConfigDao.findByCompanyIdAndDepartmentId(companyId, departmentId);
        final List<ExtraDutyRule> newRule = edv.getRules();
        final DayOffConfig doc = dayOffConfigDao.findByCompanyIdAndDepartmentId(companyId, departmentId);
        if (edc != null) {
            edc.setWorkHoursDay(edv.getWorkHoursDay());
            edc.setIsClock(edv.getIsClock());
            edc.setIsCompensation(edv.getIsCompensation());
            edc.setUpdateDate(new Date());
            final List<ExtraDutyRule> oldRule = extraDutyRuleDao.findByExtraDutyConfigId(edc.getId());
            newRule.forEach(nr -> {
                final String rule = nr.getRule();
                final String extraDutyConfigId = nr.getDepartmentId();
                oldRule.forEach(or -> {
                    final String rule1 = or.getRule();
                    final String extraDutyConfigId1 = or.getDepartmentId();
                    if (rule1.equals(rule) && extraDutyConfigId1.equals(extraDutyConfigId)) {
                        or.setIsEnable(nr.getIsEnable());
                        or.setIsTimeOff(nr.getIsTimeOff());
                        or.setRuleEndTime(nr.getRuleEndTime());
                        or.setRuleStartTime(nr.getRuleStartTime());
                        or.setUpdateDate(new Date());
                    }
                });
            });
            doc.setUpdateDate(new Date());
            doc.setLatestEffectDate(edv.getLatestEffectDate());
            doc.setUnit(edv.getUnit());
            dayOffConfigDao.save(doc);
            extraDutyRuleDao.saveAll(oldRule);
            extraDutyConfigDao.save(edc);
        } else {
            final ExtraDutyConfig extraDutyConfig = new ExtraDutyConfig();
            extraDutyConfig.setId(IdWorker.getIdStr());
            extraDutyConfig.setCompanyId(companyId);
            extraDutyConfig.setDepartmentId(departmentId);
            extraDutyConfig.setWorkHoursDay(edv.getWorkHoursDay());
            extraDutyConfig.setIsClock(edv.getIsClock());
            extraDutyConfig.setIsCompensation(edv.getIsCompensation());
            extraDutyConfig.setUpdateDate(new Date());
            extraDutyConfig.setCreateDate(new Date());
            newRule.forEach(r -> {
                r.setCompanyId(companyId);
                r.setExtraDutyConfigId(extraDutyConfig.getId());
                r.setCreateDate(new Date());
                r.setId(IdWorker.getIdStr());
                r.setUpdateDate(new Date());
            });
            final DayOffConfig dayOffConfig = new DayOffConfig();
            dayOffConfig.setCompanyId(companyId);
            dayOffConfig.setUpdateDate(new Date());
            dayOffConfig.setLatestEffectDate(edv.getLatestEffectDate());
            dayOffConfig.setUnit(edv.getUnit());
            dayOffConfig.setId(IdWorker.getIdStr());
            dayOffConfig.setDepartmentId(departmentId);
            dayOffConfig.setCreateDate(new Date());
            dayOffConfigDao.save(dayOffConfig);
            extraDutyRuleDao.saveAll(newRule);
            extraDutyConfigDao.save(extraDutyConfig);
        }
    }
}
