package com.hrm.employee.controller;


import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.utils.BeanMapUtils;
import com.hrm.domain.employee.*;
import com.hrm.employee.service.UserCompanyPersonalService;
import com.hrm.employee.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 17314
 */
@RestController
@CrossOrigin
@RequestMapping("/employees")
public class EmployeeController extends BaseController {

    private UserCompanyPersonalService userCompanyPersonalService;

    private UserCompanyJobsServiceImpl userCompanyJobsServiceImpl;

    private ResignationServiceImpl resignationServiceImpl;

    private TransferPositionServiceImpl transferPositionServiceImpl;

    private PositiveServiceImpl positiveServiceImpl;

    private ArchiveSpecServiceImpl archiveServiceImpl;

    @Autowired
    public void setUserCompanyPersonalService(UserCompanyPersonalService userCompanyPersonalService) {
        this.userCompanyPersonalService = userCompanyPersonalService;
    }

    @Autowired
    public void setUserCompanyJobsService(UserCompanyJobsServiceImpl userCompanyJobsServiceImpl) {
        this.userCompanyJobsServiceImpl = userCompanyJobsServiceImpl;
    }

    @Autowired
    public void setResignationService(ResignationServiceImpl resignationServiceImpl) {
        this.resignationServiceImpl = resignationServiceImpl;
    }

    @Autowired
    public void setTransferPositionService(TransferPositionServiceImpl transferPositionServiceImpl) {
        this.transferPositionServiceImpl = transferPositionServiceImpl;
    }

    @Autowired
    public void setPositiveService(PositiveServiceImpl positiveServiceImpl) {
        this.positiveServiceImpl = positiveServiceImpl;
    }

    @Autowired
    public void setArchiveService(ArchiveSpecServiceImpl archiveServiceImpl) {
        this.archiveServiceImpl = archiveServiceImpl;
    }

    /**
     * 员工详细信息保存
     */
    @PutMapping(value = "/{id}/personalInfo")
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        System.out.println(sourceInfo.getMobile());
        System.out.println(sourceInfo.getUsername());
        System.out.println(map);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(companyId);
        userCompanyPersonalService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工个人信息读取
     */
    @GetMapping(value = "/{id}/personalInfo")
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if(info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 员工岗位信息保存
     */
    @PutMapping(value = "/{id}/jobs")
    public Result saveJobsInfo(@PathVariable(name = "id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws Exception {
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyJobsServiceImpl.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工岗位信息读取
     */
    @GetMapping(value = "/{id}/jobs")
    public Result findJobsInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs info = userCompanyJobsServiceImpl.findById(uid);
        if(info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 离职表单保存
     */
    @PutMapping(value = "/{id}/leave")
    public Result saveLeave(@PathVariable(name = "id") String uid, @RequestBody EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationServiceImpl.save(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 离职表单读取
     */
    @GetMapping(value = "/{id}/leave")
    public Result findLeave(@PathVariable(name = "id") String uid) throws Exception {
        EmployeeResignation resignation = resignationServiceImpl.findById(uid);
        if(resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,resignation);
    }

    /**
     * 导入员工
     */
    @PostMapping(value = "/import")
    public Result importDatas(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单保存
     */
    @PutMapping(value = "/{id}/transferPosition")
    public Result saveTransferPosition(@PathVariable(name = "id") String uid, @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionServiceImpl.save(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单读取
     */
    @GetMapping(value = "/{id}/transferPosition")
    public Result findTransferPosition(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsServiceImpl.findById(uid);
        if(jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,jobsInfo);
    }

    /**
     * 转正表单保存
     */
    @PutMapping(value = "/{id}/positive")
    public Result savePositive(@PathVariable(name = "id") String uid, @RequestBody EmployeePositive positive) throws Exception {
        positiveServiceImpl.save(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 转正表单读取
     */
    @GetMapping(value = "/{id}/positive")
    public Result<EmployeePositive> findPositive(@PathVariable(name = "id") String uid) {
        EmployeePositive positive = positiveServiceImpl.findById(uid);
        if (positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result<>(ResultCode.SUCCESS, positive);
    }

    /**
     * 历史归档详情列表
     */
    @GetMapping(value = "/archives/{month}")
    public Result<Object> archives(@PathVariable(name = "month") String month, @RequestParam(name = "type") Integer type) throws Exception {
        return new Result<>(ResultCode.SUCCESS);
    }

    /**
     * 归档更新
     */
    @PutMapping(value = "/archives/{month}")
    public Result<Object> saveArchives(@PathVariable(name = "month") String month) throws Exception {
        return new Result<>(ResultCode.SUCCESS);
    }

    /**
     * 历史归档列表
     */
    @GetMapping(value = "/archives")
    public Result<PageResult<EmployeeArchive>> findArchives(@RequestParam(name = "pagesize") Integer pagesize, @RequestParam(name = "page") Integer page, @RequestParam(name = "year") String year) throws Exception {
        Map<String, Object> map = new HashMap(10);
        map.put("year", year);
        map.put("companyId", companyId);
        Page<EmployeeArchive> searchPage = archiveServiceImpl.findSearch(map, page, pagesize);
        PageResult<EmployeeArchive> pr = new PageResult<>(searchPage.getTotalElements(), searchPage.getContent());
        return new Result<PageResult<EmployeeArchive>>(ResultCode.SUCCESS, pr);
    }


}
