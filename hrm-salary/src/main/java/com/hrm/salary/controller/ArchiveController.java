package com.hrm.salary.controller;


import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.salary.SalaryArchive;
import com.hrm.domain.salary.SalaryArchiveDetail;
import com.hrm.salary.service.ArchiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 17314
 */
@RestController
@CrossOrigin
@RequestMapping("salary")
@Api(tags = "薪资归档")
@Slf4j
public class ArchiveController extends BaseController {
    private ArchiveService archiveService;

    @Autowired
    public void setArchiveService(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @GetMapping("reports/{yearMonth}")
    @ApiOperation(value = "按月获取薪资报表")
    public Result findUserSalaryInfo(@PathVariable String yearMonth, int opType, int page, int pagesize) throws Exception {
        PageResult<SalaryArchiveDetail> objectPageResult = new PageResult<>();
        List<SalaryArchiveDetail> list = new ArrayList<>(16);
        if (opType == 1) {
            // 未归档，查询当月
            objectPageResult = archiveService.getReports(yearMonth, companyId, page, pagesize);

        } else {
//            // 已归档,查询归档信息
//            final SalaryArchive archive = archiveService.findSalaryArchive(companyId, yearMonth);
//            if (archive != null) {
//                list = archiveService.findSalaryArchiveDetail(archive.getId());
//            }
        }
        return new Result(ResultCode.SUCCESS, objectPageResult);

    }

    @PostMapping("reports/{yearMonth}/archive")
    @ApiOperation(value = "归档薪资数据")
    public Result salaryArchive(@PathVariable String yearMonth) throws Exception {
        archiveService.saveArchive(yearMonth, companyId);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("reports/year")
    @ApiOperation(value = "查询薪资历史归档主数据")
    public Result salaryHistoryArchiveData(@RequestParam String year) throws Exception {
        List<SalaryArchive> list = archiveService.findAllSalaryArchive(companyId, year);
        return new Result(ResultCode.SUCCESS, list);
    }

    @PostMapping("reports/{id}")
    @ApiOperation(value = "查询薪资历史归档详情数据")
    public Result salaryHistoryArchiveDetailData(@PathVariable String id, @RequestBody Map map) throws Exception {
        PageResult<SalaryArchiveDetail> list = archiveService.findSalaryArchiveDetail(id, map);
        return new Result(ResultCode.SUCCESS, list);
    }

}
