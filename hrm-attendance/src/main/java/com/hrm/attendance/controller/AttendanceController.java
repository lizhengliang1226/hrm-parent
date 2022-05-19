package com.hrm.attendance.controller;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hrm.attendance.service.ArchiveService;
import com.hrm.attendance.service.AttendanceService;
import com.hrm.attendance.service.ExcelImportService;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.utils.DateUtils;
import com.hrm.domain.attendance.entity.Attendance;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.attendance.vo.ArchiveMonthlyVO;
import com.hrm.domain.attendance.vo.AtteUploadVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private ArchiveService archiveService;

    @Autowired
    public void setArchiveService(ArchiveService archiveService) {
        this.archiveService = archiveService;
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

    @GetMapping
    @ApiOperation(value = "查询考勤记录列表")
    public Result findAttendanceList(int page, int pagesize) throws Exception {
        final Map atteDate = attendanceService.getAtteDate(companyId, page, pagesize);
        return new Result(ResultCode.SUCCESS, atteDate);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改考勤记录")
    public Result updateAttendance(@RequestBody Attendance attendance) throws Exception {
        attendanceService.editAtte(attendance);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("reports")
    @ApiOperation(value = "查询考勤归档数据")
    public Result findAttendanceArchive(String atteDate) throws Exception {
        List<AttendanceArchiveMonthlyInfo> list = attendanceService.getReports(atteDate, companyId);
        return new Result(ResultCode.SUCCESS, list);
    }

    @GetMapping("archive/item")
    @ApiOperation(value = "归档考勤数据")
    public Result attendanceArchive(String archiveDate) throws Exception {
        archiveService.saveArchive(archiveDate, companyId);
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
        List<ArchiveMonthlyVO> list = archiveService.findAtteHistoryData(departmentId, year, companyId);
        return new Result(ResultCode.SUCCESS, list);
    }

    @PostMapping("reports/{id}")
    @ApiOperation(value = "查询考勤历史归档详情数据")
    public Result atteHistoryArchiveDetailData(@PathVariable String id) throws Exception {
        List<AttendanceArchiveMonthlyInfo> list = archiveService.findAtteHistoryDetailData(id);
        return new Result(ResultCode.SUCCESS, list);
    }

    @GetMapping("archive/{userId}/{yearMonth}")
    @ApiOperation(value = "根据用户id和月份查询考勤明细")
    public Result userAtteHistoryArchiveDetailData(@PathVariable String userId, @PathVariable String yearMonth) throws Exception {
        AttendanceArchiveMonthlyInfo archive = archiveService.findUserMonthlyDetail(userId, yearMonth);
        return new Result(ResultCode.SUCCESS, archive);
    }

    @GetMapping("aaa")
    @ApiOperation(value = "生成考勤数据")
    public Result getaaage() throws IOException, ParseException {
        List<AtteUploadVo> list = new ArrayList<>();
        for (int i = 2021; i <= 2022; i++) {
            for (int j = 1; j <= 12; j++) {
                String m = i + "";
                if (j < 10) {
                    m = m + "0" + j;
                } else {
                    m = m + j;
                }
                System.out.println(m);
                final String[] monthEveryDay = DateUtils.getMonthEveryDay(m, DatePattern.SIMPLE_MONTH_PATTERN);
                for (String s : monthEveryDay) {
                    AtteUploadVo atteUploadVo = new AtteUploadVo();
                    atteUploadVo.setAtteDate(s);
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                    final Date parse = simpleDateFormat.parse(s + " 08:00:00");
                    final Date parse1 = simpleDateFormat.parse(s + " 18:30:00");
                    atteUploadVo.setInTime(parse);
                    atteUploadVo.setOutTime(parse1);
                    atteUploadVo.setMobile("15112263614");
                    atteUploadVo.setUsername("lizhengliang");
                    atteUploadVo.setWorkNumber("2321");
                    list.add(atteUploadVo);
                }
            }
        }
        Resource template = new ClassPathResource("工作簿1.xlsx");
        final WriteSheet sheet = EasyExcel.writerSheet().build();
        FileOutputStream f = new FileOutputStream(new File("D:\\HRM-Managent\\hrm-parent\\hrm-attendance\\src\\main\\resources\\aaa.xlsx"));
        EasyExcel.write(f, AtteUploadVo.class)
                 .withTemplate(template.getInputStream()).build()
                 .fill(list, sheet)
                 .finish();
        return Result.SUCCESS();
    }

}
