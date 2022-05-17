package com.hrm.attendance.controller;

import com.hrm.attendance.service.AttendanceSettingsService;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.attendance.entity.AttendanceConfig;
import com.hrm.domain.attendance.entity.DeductionDict;
import com.hrm.domain.attendance.entity.LeaveConfig;
import com.hrm.domain.attendance.enums.LeaveTypeEnum;
import com.hrm.domain.attendance.vo.ExtDutyVO;
import com.hrm.domain.attendance.vo.ExtWorkVO;
import com.hrm.domain.attendance.vo.LeaveSetVo;
import com.lzl.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 考勤设置
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/16-17:43
 */
@RestController
@CrossOrigin
@RequestMapping("cfg")
@Api(tags = "考勤设置")
@Slf4j
public class AttendanceSettingsController extends BaseController {

    private AttendanceSettingsService attendanceSettingsService;

    @Autowired
    public void setAttendanceSettingsService(AttendanceSettingsService attendanceSettingsService) {
        this.attendanceSettingsService = attendanceSettingsService;
    }

    @PostMapping(value = "atte/item")
    @ApiOperation(value = "查询部门考勤设置")
    public Result findDeptAtteSet(@RequestBody Map map) {
        log.info("{}", map.get("departmentId"));
        final AttendanceConfig atteConfig = attendanceSettingsService.getAtteConfig(companyId, (String) map.get("departmentId"));
        return new Result(ResultCode.SUCCESS, atteConfig);
    }

    @PutMapping(value = "atte")
    @ApiOperation(value = "保存考勤设置")
    public Result saveAtteSet(@RequestBody AttendanceConfig attendanceConfig) {
        attendanceConfig.setCompanyId(companyId);
        attendanceSettingsService.saveAtteConfig(attendanceConfig);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping(value = "leave/list")
    @ApiOperation(value = "查询部门请假设置")
    public Result findDeptLeaveList(@RequestBody Map map) {
        final List<LeaveConfig> leaveList = attendanceSettingsService.getLeaveConfig(companyId, (String) map.get("departmentId"));
        List<LeaveSetVo> list = new ArrayList<>(16);
        leaveList.forEach(l -> {
            final String leaveType = LeaveTypeEnum.reverselookup(l.getLeaveType()).getCode();
            final String name = LeaveTypeEnum.reverselookup(l.getLeaveType()).getDesc();
            final LeaveSetVo leaveConfig = new LeaveSetVo(leaveType, name, l.getIsEnable(), l.getDepartmentId());
            list.add(leaveConfig);
        });
        return new Result(ResultCode.SUCCESS, list);
    }

    @PutMapping(value = "leave")
    @ApiOperation(value = "保存请假设置")
    public Result saveLeaveSet(@RequestBody List<LeaveSetVo> leaveConfigs) {
        List<LeaveConfig> list = new ArrayList<>(16);
        leaveConfigs.forEach(l -> {
            final String leaveType = LeaveTypeEnum.lookup(l.getLeaveType()).getValue();
            final LeaveConfig leaveConfig = new LeaveConfig(IdWorker.getIdStr(),
                                                            companyId, l.getDepartmentId(), leaveType,
                                                            l.getIsEnable());
            list.add(leaveConfig);
        });
        attendanceSettingsService.saveLeaveConfig(list);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping(value = "deductions/list")
    @ApiOperation(value = "查询部门扣款设置")
    public Result findDeptDeductionsList(@RequestBody Map map) {
        final List<DeductionDict> dedList = attendanceSettingsService.getDeductionConfig(companyId, (String) map.get("departmentId"));
        return new Result(ResultCode.SUCCESS, dedList);
    }

    @PutMapping(value = "deductions")
    @ApiOperation(value = "保存扣款设置")
    public Result saveDeductionsSet(@RequestBody List<DeductionDict> deductions) {
        deductions.forEach(d -> {
            d.setCompanyId(companyId);
            d.setId(IdWorker.getIdStr());
        });
        attendanceSettingsService.saveDeductionConfig(deductions);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping(value = "extraWork/item")
    @ApiOperation(value = "查询部门加班设置")
    public Result findDeptExtraWorkSet(@RequestBody Map map) {
        log.info("{}", map.get("departmentId"));
        final ExtWorkVO ewv = attendanceSettingsService.getExtraWorkConfig(companyId, (String) map.get("departmentId"));
        return new Result(ResultCode.SUCCESS, ewv);
    }

    @PutMapping(value = "extraWork")
    @ApiOperation(value = "保存考加班设置")
    public Result saveAtteSet(@RequestBody ExtDutyVO edv) {
        edv.setCompanyId(companyId);
        attendanceSettingsService.saveExtraWorkSet(edv);
        return new Result(ResultCode.SUCCESS);
    }


}
