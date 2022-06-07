package com.hrm.generate.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hrm.common.client.CompanyFeignClient;
import com.hrm.common.client.SalaryFeignClient;
import com.hrm.common.client.SocialSecurityClient;
import com.hrm.common.client.SystemFeignClient;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.utils.BankNumberUtil;
import com.hrm.common.utils.DateUtils;
import com.hrm.common.utils.RandomUserInfoUtil;
import com.hrm.common.utils.StringUtils;
import com.hrm.domain.attendance.vo.AtteUploadVo;
import com.hrm.domain.company.Department;
import com.hrm.domain.constant.Schools;
import com.hrm.domain.employee.UserCompanyPersonal;
import com.hrm.domain.employee.vo.UserVo;
import com.hrm.domain.salary.UserSalary;
import com.hrm.domain.social.CityPaymentItem;
import com.hrm.domain.social.PaymentItem;
import com.hrm.domain.social.vo.UserSocialSecurityVo;
import com.hrm.domain.system.City;
import com.hrm.domain.system.User;
import com.hrm.generate.mapper.HrmDataMapper;
import com.lzl.IdWorker;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 数据构建器
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/31-2:13
 */
@Slf4j
@RestController
@RequestMapping("build")
@ApiModel("数据生成器")
public class GenerateController extends BaseController {


    private SalaryFeignClient salaryFeignClient;
    private CompanyFeignClient companyFeignClient;
    private SystemFeignClient systemFeignClient;
    private SocialSecurityClient socialSecurityClient;

    @GetMapping("baseSalary")
    @ApiOperation(value = "批量定薪")
    public Result buildSalary() {
        Map m = new HashMap();
        m.put("companyId", companyId);
        m.put("page", 1);
        m.put("size", 10000);
        PageResult<User> data = (PageResult<User>) systemFeignClient.findAll(m).getData();
        final List<User> users = data.getRows();
        for (int i = 0; i < users.size() - 20; i++) {
            final User user = users.get(i);
            UserSalary userSalary = new UserSalary();
            final BigDecimal zz = RandomUtil.randomBigDecimal(new BigDecimal(4000), new BigDecimal(9000))
                                            .setScale(2, BigDecimal.ROUND_HALF_DOWN);

            userSalary.setUserId(user.getId());
            userSalary.setCorrectionOfBasicWages(zz);
            userSalary.setTurnToPostWages(zz);
            final BigDecimal dx = zz.multiply(new BigDecimal("0.8")).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            userSalary.setFixedPostWage(dx);
            userSalary.setFixedBasicSalary(dx);
            userSalary.setCurrentBasicSalary(dx);
            userSalary.setCurrentPostWage(dx);
            salaryFeignClient.init(userSalary);
        }
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("atteData")
    @ApiOperation(value = "生成考勤数据")
    public Result buildAtteData(@RequestBody Map map) throws IOException, ParseException {
        String filename = (String) map.get("filename");
        int startMonth = Integer.parseInt((String) map.get("startMonth"));
        int endMonth = Integer.parseInt((String) map.get("endMonth")
        );
        int startYear = Integer.parseInt((String) map.get("startYear"));
        int endYear = Integer.parseInt((String) map.get("endYear"));
        if (StrUtil.isEmpty(filename)) {
            filename = "C:\\Users\\17314\\Desktop\\HRM管理系统\\数据导入\\" + startYear + "-" + endYear + "年" + startMonth + "-" + endMonth + "月" + companyId + "考勤模拟数据.xlsx";
        } else {
            final String[] split = filename.split("\\.");
            filename = split[0] + companyId + "." + split[1];
        }
        if (ObjectUtil.isEmpty(startMonth)) {
            startMonth = 1;
        }
        if (ObjectUtil.isEmpty(endMonth)) {
            endMonth = 1;
        }
        if (ObjectUtil.isEmpty(startYear)) {
            startYear = DateUtil.year(DateUtil.date());
        }
        if (ObjectUtil.isEmpty(endYear)) {
            endYear = startYear;
        }
        buildAttendanceData(startYear, endYear, startMonth, endMonth, filename);
        return Result.SUCCESS();
    }

    @PostMapping("socialData")
    @ApiOperation(value = "生成社保数据")
    public Result buildSocialData(@RequestBody Map map1) throws IOException, ParseException {
        String filename = (String) map1.get("filename");
        filename = filename.split("\\.")[0] + companyId + "." + filename.split("\\.")[1];
        Map m = new HashMap();
        m.put("companyId", companyId);
        m.put("page", 1);
        m.put("size", 10000);
        PageResult<User> data = (PageResult<User>) systemFeignClient.findAll(m).getData();
//        final List<City> data1 = systemFeignClient.findCityList().getData();
        final List<User> rows = data.getRows();
        // 获取用户信息
        BigDecimal socialSecurityBase = BigDecimal.ZERO;
        BigDecimal providentFundBase = BigDecimal.ZERO;
        List<UserSocialSecurityVo> list = new ArrayList<>();
        for (User row : rows) {
            final UserSocialSecurityVo vo = new UserSocialSecurityVo();
            final String workingCity = row.getWorkingCity();
//            final City city = RandomUtil.randomEle(data1);
            // 生成随机社保基数和公积金基数
            vo.setSocialSecurityType(RandomUtil.randomEle(new String[]{"首次开户", "非首次开户"}));
            vo.setHouseholdRegistrationType(RandomUtil.randomEle(new String[]{"本市城镇", "本市农村", "外阜城镇", "外阜农村"}));
            socialSecurityBase = RandomUtil.randomBigDecimal(new BigDecimal(10000), new BigDecimal(20000)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            providentFundBase = socialSecurityBase;
            vo.setSocialSecurityBase(socialSecurityBase);
            vo.setProvidentFundBase(providentFundBase);
            vo.setUserId(row.getId());
            vo.setMobile(row.getMobile());
            vo.setUsername(row.getUsername());
            vo.setProvidentFundCity(workingCity);
            vo.setParticipatingInTheCity(workingCity);
            vo.setIndustrialInjuryRatio(
                    RandomUtil.randomBigDecimal(new BigDecimal("0.5"), new BigDecimal(2)).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            vo.setEnterpriseProportion(RandomUtil.randomBigDecimal(new BigDecimal("5"), new BigDecimal(12)).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            vo.setPersonalProportion(RandomUtil.randomBigDecimal(new BigDecimal("5"), new BigDecimal(12)).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            list.add(vo);
            System.out.println(vo);
        }
        Resource template = new ClassPathResource("社保数据.xlsx");
        final WriteSheet sheet = EasyExcel.writerSheet().build();
        FileOutputStream f = new FileOutputStream(
                filename);
        EasyExcel.write(f, UserSocialSecurityVo.class)
                 .withTemplate(template.getInputStream()).build()
                 .fill(list, sheet)
                 .finish();
        return Result.SUCCESS();
    }

    @GetMapping(value = "citySocial")
    @ApiOperation(value = "随机设置城市社保的缴纳比例")
    public Result buildCitySocial() throws Exception {
        final List<PaymentItem> all = socialSecurityClient.findSocialPaymentItems().getData();
        final List<City> all1 = systemFeignClient.findCityList().getData();
        all1.forEach(city -> {
            final String id = city.getId();
            all.forEach(cityPay -> {
                final String cityPayName = cityPay.getName();
                final Boolean switchCompany = cityPay.getSwitchCompany();
                final Boolean switchPersonal = cityPay.getSwitchPersonal();
                final CityPaymentItem cityPaymentItem = new CityPaymentItem();
                cityPaymentItem.setId(IdWorker.getIdStr());
                cityPaymentItem.setCityId(id);
                cityPaymentItem.setPaymentItemId(cityPay.getId());
                cityPaymentItem.setSwitchCompany(switchCompany);
                cityPaymentItem.setSwitchPersonal(switchPersonal);

                if ("养老".equals(cityPayName)) {
                    cityPaymentItem.setScaleCompany(new BigDecimal(16));
                    cityPaymentItem.setScalePersonal(new BigDecimal(8));
                }
                if ("医疗".equals(cityPayName)) {
                    cityPaymentItem.setScaleCompany(
                            RandomUtil.randomBigDecimal(new BigDecimal(7), new BigDecimal("11.7")).setScale(1, BigDecimal.ROUND_HALF_DOWN));
                    cityPaymentItem.setScalePersonal(new BigDecimal(2));
                }
                if ("失业".equals(cityPayName)) {
                    cityPaymentItem.setScaleCompany(new BigDecimal("0.5"));
                    cityPaymentItem.setScalePersonal(new BigDecimal("0.5"));
                }
                if ("工伤".equals(cityPayName)) {
                    cityPaymentItem.setScaleCompany(
                            RandomUtil.randomBigDecimal(new BigDecimal("0.2"), new BigDecimal("0.7")).setScale(2, BigDecimal.ROUND_HALF_DOWN));
                    cityPaymentItem.setScalePersonal(BigDecimal.ZERO);
                }
                if ("生育".equals(cityPayName)) {
                    cityPaymentItem.setScaleCompany(
                            RandomUtil.randomBigDecimal(new BigDecimal(0), new BigDecimal("0.7")).setScale(1, BigDecimal.ROUND_HALF_DOWN));
                    cityPaymentItem.setScalePersonal(BigDecimal.ZERO);
                }
                if ("大病".equals(cityPayName)) {
                    cityPaymentItem.setScaleCompany(BigDecimal.ZERO);
                    cityPaymentItem.setScalePersonal(BigDecimal.ZERO);
                }
                cityPaymentItem.setName(cityPayName);
                try {
                    socialSecurityClient.saveCitySocialPaymentItems(cityPaymentItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping("userBaseData")
    @ApiOperation(value = "生成基础用户数据")
    public Result buildUserBaseData(@RequestBody Map map) throws IOException {
        final int num = Integer.parseInt((String) map.get("num"));
        String filename = (String) map.get("filename");
        filename = filename.split("\\.")[0] + companyId + "." + filename.split("\\.")[1];
        final List<Department> depts = companyFeignClient.findAll().getData().getDepts();
        final List<City> data = systemFeignClient.findCityList().getData();
        List<UserVo> list = new ArrayList<>(num);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < num; i++) {
            final UserVo userVo = new UserVo();
            final Department department = RandomUtil.randomEle(depts);
            final City city = RandomUtil.randomEle(data);
            userVo.setUsername(RandomUserInfoUtil.getChineseName());
            userVo.setMobile(RandomUserInfoUtil.getTelephone());
            userVo.setDepartmentCode(department.getCode());
            userVo.setFormOfEmployment(1);
            userVo.setGender(RandomUtil.randomEle(new String[]{"1", "2"}));
            userVo.setFormOfManagement(String.valueOf(1));
            final DateTime dateTime = RandomUtil.randomDate(new Date(), DateField.MONTH, -3, 2);
            userVo.setTimeOfEntry(dateTime);
            userVo.setWorkingCity(city.getName());
            userVo.setWorkNumber(String.valueOf(RandomUtil.randomInt(10000, 99999)));
            list.add(userVo);
        }

        Resource template = new ClassPathResource("用户批量添加构造模板.xlsx");
        final WriteSheet sheet = EasyExcel.writerSheet().build();
        FileOutputStream f = new FileOutputStream(new File(filename));
        EasyExcel.write(f, AtteUploadVo.class)
                 .withTemplate(template.getInputStream()).build()
                 .fill(list, sheet)
                 .finish();
        return Result.SUCCESS();
    }

    // 考勤数据
    private void buildAttendanceData(int startYear, int endYear, int startMonth, int endMonth, String filename) throws ParseException, IOException {
        final String[] split = filename.split("\\.");
        System.out.println(split);
        List<AtteUploadVo> list = new ArrayList<>();
        String[] startHours = new String[]{"08", "09"};
        String[] endHours = new String[]{"18", "19"};
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        final Map map1 = new HashMap<>();
        map1.put("page", 1);
        map1.put("size", 10000);
        final Result all = systemFeignClient.findAll(map1);
        final PageResult<User> users = (PageResult<User>) all.getData();
        for (User user : users.getRows()) {
            final String mobile = user.getMobile();
            final String workNumber = user.getWorkNumber();
            final String username = user.getUsername();
            for (int i = startYear; i <= endYear; i++) {
                for (int j = startMonth; j <= endMonth; j++) {
                    String m = i + "";
                    if (j < 10) {
                        m = m + "0" + j;
                    } else {
                        m = m + j;
                    }
                    final String[] monthEveryDay = DateUtils.getMonthEveryDay(m, DatePattern.SIMPLE_MONTH_PATTERN);
                    for (String s : monthEveryDay) {
                        AtteUploadVo atteUploadVo = new AtteUploadVo();
                        atteUploadVo.setAtteDate(s);
                        final int t1 = RandomUtil.randomInt(0, 60);
                        final int t2 = RandomUtil.randomInt(0, 60);
                        final int t3 = RandomUtil.randomInt(0, 60);
                        final int t4 = RandomUtil.randomInt(0, 60);
                        String t11 = "";
                        String t22 = "";
                        String t33 = "";
                        String t44 = "";
                        t11 = t1 < 10 ? "0" + t1 : t1 + "";
                        t22 = t2 < 10 ? "0" + t2 : t2 + "";
                        t33 = t3 < 10 ? "0" + t3 : t3 + "";
                        t44 = t4 < 10 ? "0" + t4 : t4 + "";
                        final Date intime = simpleDateFormat.parse(s + " " + RandomUtil.randomEle(startHours) + ":" + t11 + ":" + t22);
                        final Date outtime = simpleDateFormat.parse(s + " " + RandomUtil.randomEle(endHours) + ":" + t33 + ":" + t44);
                        atteUploadVo.setInTime(intime);
                        atteUploadVo.setOutTime(outtime);
                        atteUploadVo.setMobile(mobile);
                        atteUploadVo.setUsername(username);
                        atteUploadVo.setWorkNumber(workNumber);
                        list.add(atteUploadVo);
                    }
                }
            }
        }
        Resource template = new ClassPathResource("考勤数据构建模板.xlsx");
        final WriteSheet sheet = EasyExcel.writerSheet().build();
        FileOutputStream f = new FileOutputStream(filename);
        EasyExcel.write(f, AtteUploadVo.class)
                 .withTemplate(template.getInputStream()).build()
                 .fill(list, sheet)
                 .finish();
    }


    @PostMapping("userDetailData")
    @ApiOperation(value = "生成用户详细信息数据")
    public void generateUserDetailInfo(@RequestBody Map map) throws IOException {
        String filename = (String) map.get("filename");
        filename = filename.split("\\.")[0] + companyId + "." + filename.split("\\.")[1];
        // 生成用户详细信息
        final Map map1 = new HashMap<>();
        map1.put("page", 1);
        map1.put("size", 10000);
        final Result all = systemFeignClient.findAll(map1);
        final PageResult<User> data = (PageResult<User>) all.getData();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat s1 = new SimpleDateFormat("yyyyMMdd");
        List<UserCompanyPersonal> list = new ArrayList<>();
        for (User row : data.getRows()) {
            final UserCompanyPersonal sourceInfo = new UserCompanyPersonal();
            final DateTime birth = RandomUtil.randomDate(new DateTime("1994-01-01"), DateField.YEAR, -6, 7);
            sourceInfo.setPoliticalOutlook("共青团员");
            sourceInfo.setBirthday(DateUtil.format(birth, DatePattern.PURE_DATE_PATTERN));
            sourceInfo.setUserId(row.getId());
            sourceInfo.setCompanyId(companyId);
            sourceInfo.setUsername(row.getUsername());
            sourceInfo.setDepartmentName(row.getDepartmentName());
            sourceInfo.setMobile(row.getMobile());
            final long age = DateUtil.betweenYear(birth, DateUtil.date(), true);
            sourceInfo.setAge((int) age);
            final DateTime dateTime = RandomUtil.randomDate(birth, DateField.YEAR, 20, (int) age);
            sourceInfo.setTimeToJoinTheParty(s1.format(dateTime));
            sourceInfo.setBankCardNumber(String.valueOf(RandomUtil.randomLong(1000000000000000L, 9999999999999999L)));
            final Date timeOfEntry = row.getTimeOfEntry();
            sourceInfo.setTimeOfEntry(s.format(timeOfEntry));
            sourceInfo.setAreThereAnyMajorMedicalHistories("无");
            sourceInfo.setDetailAddress("湖南省长沙市天心区");
            sourceInfo.setEmergencyContact(RandomUserInfoUtil.getChineseName());
            sourceInfo.setEmergencyContactNumber(RandomUserInfoUtil.getTelephone());
            sourceInfo.setGraduateSchool(RandomUtil.randomEle(Schools.SCHOOLS));
            final DateTime entryTime = DateUtil.offset(birth, DateField.YEAR, 18);
            final DateTime endTime = DateUtil.offset(birth, DateField.YEAR, 22);
            sourceInfo.setEnrolmentTime(s.format(entryTime));
            sourceInfo.setGraduationTime(s.format(endTime));
            sourceInfo.setHomeCompany("A公司");
            sourceInfo.setTitle("职员");
            sourceInfo.setIsThereAnyCompetitionRestriction("2");
            sourceInfo.setRemarks("无");
            sourceInfo.setTheHighestDegreeOfEducation(RandomUtil.randomEle(new String[]{"本科", "硕士"}));
            sourceInfo.setNationalArea("中国大陆");
            final String idNo = StringUtils.getIdNo(DateUtil.format(birth, DatePattern.PURE_DATE_PATTERN), "1".equals(row.getGender()));
            sourceInfo.setIdNumber(idNo);
            sourceInfo.setNativePlace("430000/430100");
            sourceInfo.setNation(RandomUtil.randomEle(new String[]{"汉族", "布依族", "壮族", "回族", "苗族", "满族"}));
            sourceInfo.setMaritalStatus(RandomUtil.randomEle(new String[]{"未婚", "已婚"}));
            sourceInfo.setBloodType(RandomUtil.randomEle(new String[]{"A型", "B型", "AB型", "O型"}));
            sourceInfo.setDomicile("430000/430100/430103");
            sourceInfo.setQq(String.valueOf(RandomUtil.randomInt(10000000, 999999999)));
            sourceInfo.setWechat(sourceInfo.getQq());
            sourceInfo.setPlaceOfResidence("430000/430100/430103");
            sourceInfo.setContactTheMobilePhone(row.getMobile());
            sourceInfo.setPersonalMailbox(RandomUserInfoUtil.getEmail(8, 10));
            sourceInfo.setSocialSecurityComputerNumber(sourceInfo.getIdNumber());
            sourceInfo.setProvidentFundAccount(String.valueOf(RandomUtil.randomLong(100000000L, 999999999L)));
            sourceInfo.setBankCardNumber(BankNumberUtil.getBankNumber(RandomUtil.randomEle(new String[]{"6", "8", "9"})));
            sourceInfo.setOpeningBank("平安银行");
            sourceInfo.setEducationalType("统招");
            sourceInfo.setMajor("030000/031600/031601");
            log.info("{}", sourceInfo);
            list.add(sourceInfo);
        }
        Resource template = new ClassPathResource("/template/员工月度报表导出模板.xlsx");
        final WriteSheet sheet = EasyExcel.writerSheet().build();
        FileOutputStream f = new FileOutputStream(filename);
        EasyExcel.write(f, UserCompanyPersonal.class)
                 .withTemplate(template.getInputStream()).build()
                 .fill(list, sheet)
                 .finish();
    }

    @Autowired
    private HrmDataMapper hrmDataMapper;

    @GetMapping("clearCompanyData")
    @ApiModelProperty("清除企业数据")
    public Result clearData(@RequestParam String companyId) {
        final Integer integer = hrmDataMapper.deleteData(companyId);
        return new Result(ResultCode.SUCCESS, integer);
    }

    @Autowired
    public void setSalaryFeignClient(SalaryFeignClient salaryFeignClient) {
        this.salaryFeignClient = salaryFeignClient;
    }

    @Autowired
    public void setCompanyFeignClient(CompanyFeignClient companyFeignClient) {
        this.companyFeignClient = companyFeignClient;
    }

    @Autowired
    public void setSystemFeignClient(SystemFeignClient systemFeignClient) {
        this.systemFeignClient = systemFeignClient;
    }

    @Autowired
    public void setSocialSecurityClient(SocialSecurityClient socialSecurityClient) {
        this.socialSecurityClient = socialSecurityClient;
    }
}
