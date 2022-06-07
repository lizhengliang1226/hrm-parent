package com.hrm.social.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hrm.common.client.EmployeeFeignClient;
import com.hrm.common.client.SalaryFeignClient;
import com.hrm.common.client.SystemFeignClient;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.social.*;
import com.hrm.domain.social.vo.UserSocialSecuritySimpleVo;
import com.hrm.domain.system.City;
import com.hrm.social.service.ArchiveService;
import com.hrm.social.service.CompanySettingsService;
import com.hrm.social.service.PaymentItemService;
import com.hrm.social.service.UserSocialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社保微服务
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/15-10:03
 */
@RestController
@CrossOrigin
@RequestMapping("social")
@Api(tags = "社保管理微服务")
@Slf4j
public class SocialSecurityController extends BaseController {
    private CompanySettingsService companySettingsService;
    private UserSocialService userSocialService;
    private SystemFeignClient systemFeignClient;
    private EmployeeFeignClient emFeignClient;
    private PaymentItemService paymentItemService;
    private ArchiveService archiveService;
    private RedisTemplate redisTemplate;
    private SalaryFeignClient salaryFeignClient;

    @Value("${social-month-template-path}")
    private String templateName;


    @RequiresPermissions("API_SOCIAL_MONTH_FIND")
    @GetMapping(value = "settings")
    @ApiOperation(value = "查询企业是否设置社保")
    public Result<CompanySettings> findSettings() {
        final CompanySettings byId = companySettingsService.findById(companyId);
        return new Result<>(ResultCode.SUCCESS, byId);
    }

    @PostMapping(value = "settings")
    @ApiOperation(value = "保存企业社保设置")
    public Result saveSettings(@RequestBody CompanySettings companySettings) {
        companySettings.setCompanyId(companyId);
        companySettingsService.save(companySettings);
        return new Result(ResultCode.SUCCESS);
    }


    @PostMapping(value = "list")
    @ApiOperation(value = "查询企业员工的社保信息列表")
    public Result findSocialList(@RequestBody Map map) {
        map.put("companyId", companyId);
        final PageResult<UserSocialSecuritySimpleVo> all = userSocialService.findAll(map);
        return new Result(ResultCode.SUCCESS, all);
    }

    @GetMapping(value = "tips/{yearMonth}")
    @ApiOperation(value = "获取企业社保月度基础信息")
    public Result findBaseInfo(@PathVariable(value = "yearMonth") String yearMonth) {
        //todo
        Map map = new HashMap(8);
        map.put("dateRange", yearMonth);
        map.put("socialSecurityCount", 10);
        map.put("providentFundCount", 38);
        map.put("newsCount", 23);
        map.put("reducesCount", 10);
        map.put("worksCount", 190);
        map.put("leavesCount", 33);
        log.info(yearMonth);
        log.info("{}", map);
        return new Result(ResultCode.SUCCESS, map);
    }

    @RequiresPermissions("API_SOCIAL_DETAIL_FIND")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询用户社保数据")
    public Result<Map<String, Object>> findUserSocialInfo(@PathVariable(value = "id") String userId) throws Exception {
        Map map = new HashMap(2);
        // 用户信息
        final Object user = systemFeignClient.findById(userId).getData();
        // 薪资数据
        final Object salary = salaryFeignClient.findUserSalary(userId).getData();
        // 用户社保信息
        UserSocialSecurity byId = userSocialService.findById(userId);
        byId.setUserId(userId);
        map.put("user", user);
        map.put("salary", salary);
        map.put("userSocialSecurity", byId);
        return new Result(ResultCode.SUCCESS, map);
    }

    @GetMapping(value = "/paymentItem/{id}")
    @ApiOperation(value = "查询某个市社保数据")
    public Result findCitySocialInfo(@PathVariable(value = "id") String cityId) {
        List<CityPaymentItem> list = paymentItemService.findAllByCityId(cityId);
        return new Result(ResultCode.SUCCESS, list);
    }

    @RequiresPermissions("API_SOCIAL_DETAIL_UPDATE")
    @PutMapping
    @ApiOperation(value = "保存或更新用户社保数据")
    public Result saveUserSocialInfo(@RequestBody UserSocialSecurity userSocialInfo) {
        userSocialService.save(userSocialInfo);
        return new Result(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "导出员工社保信息月度报表")
    @GetMapping(value = "/export/{month}")
    public void export(@PathVariable String month, HttpServletResponse response) throws Exception {
        final PageResult<SocialSecrityArchiveDetail> reports = archiveService.getReports(month, companyId, null, null);
        final List<SocialSecrityArchiveDetail> rows = reports.getRows();
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
        EasyExcel.write(response.getOutputStream(), SocialSecrityArchiveDetail.class)
                 .withTemplate(template.getInputStream()).build()
                 .fill(map, sheet)
                 .fill(rows, sheet)
                 .finish();
    }

    @GetMapping(value = "historys/{yearMonth}")
    @ApiOperation(value = "查询当月或历史员工社保信息")
    public Result historyDetail(@PathVariable String yearMonth, int opType, Integer page, Integer pagesize) throws Exception {
        PageResult<SocialSecrityArchiveDetail> list = null;
        if (opType == 1) {
            // 未归档，查询当月
            list = archiveService.getReports(yearMonth, companyId, page, pagesize);
        } else {
            // 已归档,查询归档信息
            final Archive archive = archiveService.findArchive(companyId, yearMonth);
            if (archive != null) {
                list = archiveService.findAllDetailByArchiveId(archive.getId(), page, pagesize);
            }
        }
        return new Result(ResultCode.SUCCESS, list);
    }

    @PostMapping(value = "historys/{yearMonth}/archive")
    @ApiOperation(value = "社保归档")
    public Result monthArchive(@PathVariable String yearMonth) throws Exception {
        final long start = System.currentTimeMillis();
        archiveService.archive(yearMonth, companyId);
        final long end = System.currentTimeMillis();
        log.info("社保归档总用时：{}ms", end - start);
        return new Result(ResultCode.SUCCESS);
    }

    @PutMapping(value = "historys/{yearMonth}/newReport")
    @ApiOperation(value = "制作新报表，切换统计周期")
    public Result newReport(@PathVariable String yearMonth) throws Exception {
        CompanySettings cs = companySettingsService.findById(companyId);
        if (cs == null) {
            cs = new CompanySettings();
        }
        cs.setCompanyId(companyId);
        cs.setDataMonth(yearMonth);
        companySettingsService.save(cs);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping(value = "historys/{year}/list")
    @ApiOperation(value = "查询历史归档主档列表")
    public Result historyArchiveList(@PathVariable String year) throws Exception {
        final List<Archive> archiveByYear = archiveService.findArchiveByYear(year, companyId);
        return new Result(ResultCode.SUCCESS, archiveByYear);
    }

    @GetMapping(value = "historys/data")
    @ApiOperation(value = "根据用户id和年月查询归档明细")
    public Result userHistoryArchiveData(@RequestBody Map<String, String> map) throws Exception {
        SocialSecrityArchiveDetail socialSecrityArchiveDetail = archiveService.findUserArchiveDetail(map.get("userId"), map.get("yearMonth"));
        return new Result(ResultCode.SUCCESS, socialSecrityArchiveDetail);
    }


    @PostMapping(value = "import")
    @ApiOperation(value = "社保导入")
    public Result importSocial(@RequestParam MultipartFile file) throws Exception {
        userSocialService.importSocialExcel(file, companyId);
        return Result.SUCCESS();
    }

    @GetMapping(value = "paymentItem")
    @ApiOperation(value = "查询社保缴费字典项")
    public Result<List<PaymentItem>> findSocialPaymentItems() throws Exception {
        final List<PaymentItem> allPaymentItems = paymentItemService.findAllPaymentItems();
        return new Result(ResultCode.SUCCESS, allPaymentItems);
    }

    @GetMapping(value = "buildCityPayRedis")
    @ApiOperation(value = "构建城市社保项缓存")
    public Result buildCityPayRedis() throws Exception {
        final List<City> data = systemFeignClient.findCityList().getData();
        for (City city : data) {
            final String id = city.getId();
            final List<CityPaymentItem> allByCityId = paymentItemService.findAllByCityId(id);
            redisTemplate.boundHashOps(SystemConstant.REDIS_CITY_PAYMENT_LIST).put(id, allByCityId);
        }
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping(value = "savePaymentItem")
    @ApiOperation(value = "保存城市社保缴费字典项")
    public Result saveCitySocialPaymentItems(@RequestBody CityPaymentItem cityPay) throws Exception {
        paymentItemService.saveCityPaymentItem(cityPay);
        return new Result(ResultCode.SUCCESS);
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setArchiveService(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @Autowired
    public void setPaymentItemService(PaymentItemService paymentItemService) {
        this.paymentItemService = paymentItemService;
    }

    @Autowired
    public void setSystemFeignClient(SystemFeignClient systemFeignClient) {
        this.systemFeignClient = systemFeignClient;
    }

    @Autowired
    public void setEmFeignClient(EmployeeFeignClient emFeignClient) {
        this.emFeignClient = emFeignClient;
    }

    @Autowired
    public void setUserSocialService(UserSocialService userSocialService) {
        this.userSocialService = userSocialService;
    }

    @Autowired
    public void setCompanySettingsService(CompanySettingsService companySettingsService) {
        this.companySettingsService = companySettingsService;
    }

    @Autowired
    public void setSalaryFeignClient(SalaryFeignClient salaryFeignClient) {
        this.salaryFeignClient = salaryFeignClient;
    }
}
