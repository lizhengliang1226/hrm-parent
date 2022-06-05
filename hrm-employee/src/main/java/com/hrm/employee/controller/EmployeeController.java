package com.hrm.employee.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hrm.common.cache.SystemCache;
import com.hrm.common.client.SystemFeignClient;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.utils.BeanMapUtils;
import com.hrm.domain.attendance.entity.User;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.employee.*;
import com.hrm.domain.employee.response.EmployeeReportResult;
import com.hrm.employee.service.UserCompanyPersonalService;
import com.hrm.employee.service.impl.*;
import com.lzl.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 17314
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/employees")
@Api(tags = "员工管理")
public class EmployeeController extends BaseController {

    private UserCompanyPersonalService userCompanyPersonalService;

    private UserCompanyJobsServiceImpl userCompanyJobsServiceImpl;

    private ResignationServiceImpl resignationServiceImpl;

    private TransferPositionServiceImpl transferPositionServiceImpl;

    private PositiveServiceImpl positiveServiceImpl;

    private ArchiveServiceImpl archiveServiceImpl;
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(16, 16, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), (r) -> new Thread(r, "t1"));

    @Value("${employee-month-template-path}")
    private String templateName;
    @Value("${employee-pdf-template-path}")
    private String pdfTemplateName;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 员工信息pdf报表导出
     *
     * @param response 响应
     * @param request  请求
     */
    @GetMapping("{id}/pdf")
    public void exportPdf(HttpServletResponse response, HttpServletRequest request, @PathVariable String id) throws Exception {
        // 构造数据
        final HashMap<String, Object> map = new HashMap<>(32);
        final UserCompanyPersonal userCompanyPersonal = userCompanyPersonalService.findById(id);
        final UserCompanyJobs userCompanyJobs = userCompanyJobsServiceImpl.findById(id);
        String staffPhoto = userCompanyPersonal.getStaffPhoto();
        map.put("staffPhoto", staffPhoto);
        final Map<String, Object> map1 = BeanMapUtils.beanToMap(userCompanyJobs);
        final Map<String, Object> map2 = BeanMapUtils.beanToMap(userCompanyPersonal);
        map.putAll(map1);
        map.putAll(map2);
        // 设置响应类型
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // 填充数据
        Resource resource = new ClassPathResource(pdfTemplateName);
        FileInputStream fileInputStream = new FileInputStream(resource.getFile());
        final JasperPrint jasperPrint = JasperFillManager.fillReport(fileInputStream, map, new JREmptyDataSource());
        // 响应数据
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    /**
     * 员工详细信息保存
     */
    @PutMapping(value = "/{id}/personalInfo")
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        log.info("{}", sourceInfo);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }

        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(companyId);
        userCompanyPersonalService.save(sourceInfo);
//        redisTemplate.boundHashOps("userDetailList").put(uid, sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    @Autowired
    private SystemFeignClient stemFeignClient;

    /**
     * 员工个人信息读取
     */
    @GetMapping(value = "/{id}/personalInfo")
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if (info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, info);
    }

    /**
     * 员工岗位信息保存
     */
    @PutMapping(value = "/{id}/jobs")
    public Result saveJobsInfo(@PathVariable(name = "id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws
            Exception {
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
        if (info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS, info);
    }

    /**
     * 离职表单保存
     */
    @PutMapping(value = "/{id}/leave")
    public Result saveLeave(@PathVariable(name = "id") String uid, @RequestBody EmployeeResignation resignation) throws
            Exception {
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
        if (resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, resignation);
    }

    /**
     * 导入员工
     */
    @PostMapping(value = "/import")
    public Result importDatas(@RequestParam(name = "file") MultipartFile file) throws Exception {
        final ExcelReaderBuilder read = EasyExcel.read(file.getInputStream(), UserCompanyPersonal.class, new UserDetailExcelListener());
        final ExcelReaderSheetBuilder sheet = read.sheet();
        sheet.doRead();
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * Excel操作的内部类
     */
    class UserDetailExcelListener extends AnalysisEventListener<UserCompanyPersonal> {


        @Override
        public void invoke(UserCompanyPersonal user, AnalysisContext analysisContext) {
            User user1 = SystemCache.USER_INFO_CACHE.get(user.getMobile());
            if (user1 == null) {
                user1 = (User) redisTemplate.boundHashOps(SystemConstant.REDIS_USER_LIST).get(user.getMobile());
                SystemCache.USER_INFO_CACHE.put(user.getMobile(), user1);
            }
            user.setUserId(user1.getId());
            user.setCompanyId(companyId);
            pool.execute(() -> {
                userCompanyPersonalService.save(user);
            });
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {

        }

    }

    /**
     * 调岗表单保存
     */
    @PutMapping(value = "/{id}/transferPosition")
    public Result saveTransferPosition(@PathVariable(name = "id") String uid,
                                       @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
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
        if (jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, jobsInfo);
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
        log.info("{}", uid);
        EmployeePositive positive = positiveServiceImpl.findById(uid);
        if (positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result<>(ResultCode.SUCCESS, positive);
    }

    /**
     * 历史归档详情列表
     * 某个月的离职与入职人员列表
     * type: 1-全部 2-离职 3-入职
     */
    @GetMapping(value = "/archives/{yearMonth}")
    public Result archives(@PathVariable String yearMonth,
                           @RequestParam Integer type,
                           @RequestParam Integer page,
                           @RequestParam Integer pagesize) throws Exception {
        //根据月份和类型查询对应的离职和入职人员列表信息
        if (type == 1) {
            // 查询该月全部
            // 获取月度报表数据
            Page<Map> list = userCompanyPersonalService.findMonthlyReport(companyId, yearMonth.substring(0, 4) + "-" + yearMonth.substring(4), page,
                                                                          pagesize);
            PageResult<Map> pageResult = new PageResult<>(list.getTotalElements(), list.getContent());
            return new Result(ResultCode.SUCCESS, pageResult);
        } else if (type == 2) {
            // 查询离职
        } else if (type == 3) {
            // 查询入职
        } else {
            return new Result<>(ResultCode.FAIL);
        }


        return new Result<>(ResultCode.SUCCESS);
    }

    /**
     * 点击归档，归档更新
     */
    @PutMapping(value = "/archives/{month}")
    public Result<Object> saveArchives(@PathVariable String month) throws Exception {
        final String month1 = month.substring(0, 4) + "-" + month.substring(4);
        EmployeeArchive e1 = archiveServiceImpl.findByMonth(month);
        // 查询总人数
        Page<Map> list = userCompanyPersonalService.findMonthlyReport(companyId, month1, 1, 9999);
        // 查询在职人数
        final Integer onJob = userCompanyPersonalService.numOfJobStatus(companyId, month1, 1);
        // 查询离职人数
        Integer num = resignationServiceImpl.findDepartureNum(month1, companyId);
        if (e1 != null) {
            // 已归档，则将原归档覆盖
            e1.setCompanyId(companyId);
            e1.setDepartures(num);
            e1.setTotals((int) list.getTotalElements());
            e1.setPayrolls(onJob);
            e1.setOpUser(username);
            archiveServiceImpl.save(e1);
        } else {
            // 未归档新建归档
            EmployeeArchive e = new EmployeeArchive();
            e.setCompanyId(companyId);
            e.setOpUser(username);
            e.setDepartures(num);
            e.setMonth(month);
            e.setTotals((int) list.getTotalElements());
            e.setPayrolls(onJob);
            e.setId(IdWorker.getIdStr());
            archiveServiceImpl.save(e);
        }
        return new Result<>(ResultCode.SUCCESS);
    }

    /**
     * 历史归档主列表
     */
    @GetMapping(value = "/archives")
    public Result findArchives(@RequestParam String year) throws Exception {
        List<EmployeeArchive> searchPage = archiveServiceImpl.findSearch(year + "%", companyId);
        return new Result<>(ResultCode.SUCCESS, searchPage);
    }

    @ApiOperation(value = "导出员工月度报表")
    @GetMapping(value = "/export/{month}")
    public void export(@PathVariable String month, HttpServletResponse response) throws Exception {
        // 获取月度报表数据
        Page<Map> list = userCompanyPersonalService.findMonthlyReport(companyId, month.substring(0, 4) + "-" + month.substring(4), null, null);
        // 转换数据
        List<EmployeeReportResult> list1 = new ArrayList<>();
        for (Map map : list.getContent()) {
            final EmployeeReportResult err = new EmployeeReportResult();
            err.setMap(map);

            list1.add(err);
        }
        // 准备表头数据
        Map<String, Object> map = new HashMap<>();
        map.put("month", month);
        // 模板位置
        Resource template = new ClassPathResource(templateName);
        // 设置响应数据格式为流
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // 开始写入
        final WriteSheet sheet = EasyExcel.writerSheet().build();
        EasyExcel.write(response.getOutputStream(), Map.class)
                 .withTemplate(template.getInputStream()).build()
                 .fill(map, sheet)
                 .fill(list1, sheet)
                 .finish();
    }

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
    public void setArchiveService(ArchiveServiceImpl archiveServiceImpl) {
        this.archiveServiceImpl = archiveServiceImpl;
    }
}
