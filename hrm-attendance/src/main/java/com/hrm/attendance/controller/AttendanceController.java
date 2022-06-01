package com.hrm.attendance.controller;

import com.hrm.attendance.service.AtteArchiveService;
import com.hrm.attendance.service.AttendanceService;
import com.hrm.attendance.service.ExcelImportService;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.attendance.entity.Attendance;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.attendance.entity.AttendanceCompanySettings;
import com.hrm.domain.attendance.vo.ArchiveMonthlyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 考勤控制器
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/17-18:09
 */
@RestController
@CrossOrigin
@RequestMapping("attendances")
@Api(tags = "考勤管理")
@Slf4j
public class AttendanceController extends BaseController {

    private ExcelImportService excelImportService;
    private AttendanceService attendanceService;
    private AtteArchiveService atteArchiveService;

    @Autowired
    public void setArchiveService(AtteArchiveService atteArchiveService) {
        this.atteArchiveService = atteArchiveService;
    }

    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @Autowired
    public void setExcelImportService(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    @PostMapping(value = "import")
    @ApiOperation(value = "批量保存考勤数据")
    public Result importAttendance(@RequestParam MultipartFile file) throws Exception {
        excelImportService.importAttendanceExcel(file, companyId);
        return Result.SUCCESS();
    }

    @PostMapping
    @ApiOperation(value = "查询考勤记录列表")
    public Result findAttendanceList(@RequestBody Map map) throws Exception {
        map.put("companyId", companyId);
        final Map atteDate = attendanceService.getAtteData(map);
        return new Result(ResultCode.SUCCESS, atteDate);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改考勤记录")
    public Result updateAttendance(@RequestBody Attendance attendance) throws Exception {
        attendanceService.saveOrUpdateAtte(attendance);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("reports")
    @ApiOperation(value = "查询考勤报表数据")
    public Result findAttendanceReport(String atteDate, int page, int pagesize) throws Exception {
        final PageResult<AttendanceArchiveMonthlyInfo> reports = attendanceService.getReports(atteDate, companyId, page, pagesize);
        return new Result(ResultCode.SUCCESS, reports);
    }

    @GetMapping("archive/item")
    @ApiOperation(value = "归档考勤数据")
    public Result attendanceArchive(String archiveDate) throws Exception {
        atteArchiveService.saveArchive(archiveDate, companyId);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("newReports")
    @ApiOperation(value = "新建考勤报表数据")
    public Result newAtteReportData(String atteDate) throws Exception {
        attendanceService.newReport(atteDate, companyId);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("reports/year")
    @ApiOperation(value = "查询考勤历史归档数据")
    public Result atteHistoryArchiveData(String departmentId, String year) throws Exception {
        List<ArchiveMonthlyVO> list = atteArchiveService.findAtteHistoryData(departmentId, year, companyId);
        return new Result(ResultCode.SUCCESS, list);
    }

    @PostMapping("reports/{id}")
    @ApiOperation(value = "查询考勤历史归档详情数据")
    public Result atteHistoryArchiveDetailData(@PathVariable String id) throws Exception {
        List<AttendanceArchiveMonthlyInfo> list = atteArchiveService.findAtteHistoryDetailData(id);
        return new Result(ResultCode.SUCCESS, list);
    }

    @GetMapping("archive/{userId}/{yearMonth}")
    @ApiOperation(value = "根据用户id和月份查询考勤明细")
    public Result userAtteHistoryArchiveDetailData(@PathVariable String userId, @PathVariable String yearMonth) throws Exception {
        AttendanceArchiveMonthlyInfo archive = atteArchiveService.findUserMonthlyDetail(userId, yearMonth);
        return new Result(ResultCode.SUCCESS, archive);
    }

    /**
     * 获取企业是否设置考勤月份
     */
    @GetMapping(value = "/company-settings")
    public Result getCompanySettings() throws Exception {
        AttendanceCompanySettings companySettings = attendanceService.findMonthById(companyId);
        return new Result(ResultCode.SUCCESS, companySettings);
    }

    /**
     * 保存企业考勤月份设置
     */
    @PostMapping(value = "/company-settings")
    public Result saveCompanySettings(@RequestBody AttendanceCompanySettings companySettings) throws Exception {
        companySettings.setCompanyId(companyId);
        attendanceService.saveSetMonth(companySettings);
        return new Result(ResultCode.SUCCESS);
    }

}
