package com.hrm.generate.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hrm.common.client.SystemFeignClient;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.utils.DateUtils;
import com.hrm.domain.attendance.vo.AtteUploadVo;
import com.hrm.domain.employee.vo.UserVo;
import com.hrm.domain.generate.User;
import com.hrm.domain.social.vo.UserSocialSecurityVo;
import com.hrm.domain.system.City;
import com.hrm.generate.dao.UserDao;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("build")
public class GenerateController extends BaseController {
    @Autowired
    private SystemFeignClient systemFeignClient;
    @Autowired
    private UserDao userDao;

    @GetMapping("atteData")
    @ApiOperation(value = "生成数据")
    public Result buildAtteData(@RequestBody Map map) throws IOException, ParseException {
        String filename = (String) map.get("filename");
        int startMonth = Integer.parseInt((String) map.get("startMonth"));
        int endMonth = Integer.parseInt((String) map.get("endMonth")
        );
        int startYear = Integer.parseInt((String) map.get("startYear"));
        int endYear = Integer.parseInt((String) map.get("endYear"));
        if (StrUtil.isEmpty(filename)) {
            filename = "C:\\Users\\17314\\Desktop\\HRM管理系统\\数据导入\\" + startYear + "-" + endYear + "年" + startMonth + "-" + endMonth + "月考勤模拟数据.xlsx";
        }
        if (ObjectUtil.isEmpty(startMonth)) {
            startMonth = 1;
        }
        if (ObjectUtil.isEmpty(endMonth)) {
            endMonth = 1;
        }
        if (ObjectUtil.isEmpty(startYear)) {
            startYear = DateUtil.date().getYear();
        }
        if (ObjectUtil.isEmpty(endYear)) {
            endYear = startYear;
        }
        buildAttendanceData(startYear, endYear, startMonth, endMonth, filename);
        return Result.SUCCESS();
    }

    @GetMapping("socialData")
    @ApiOperation(value = "生成社保数据")
    public Result buildSocialData() throws IOException, ParseException {
        Map m = new HashMap();
        m.put("companyId", "1");
        m.put("page", 1);
        m.put("size", 10000);
        PageResult<User> data = (PageResult<User>) systemFeignClient.findAll(m).getData();
        final List<City> data1 = systemFeignClient.findAll().getData();
        final List<User> rows = data.getRows();
        // 获取用户信息
        BigDecimal socialSecurityBase = BigDecimal.ZERO;
        BigDecimal providentFundBase = BigDecimal.ZERO;
        List<UserSocialSecurityVo> list = new ArrayList<>();
        for (User row : rows) {
            final UserSocialSecurityVo vo = new UserSocialSecurityVo();
            final City city = RandomUtil.randomEle(data1);
            // 生成随机社保基数和公积金基数
            socialSecurityBase = RandomUtil.randomBigDecimal(new BigDecimal(10000), new BigDecimal(20000)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            providentFundBase = socialSecurityBase;
            vo.setSocialSecurityBase(socialSecurityBase);
            vo.setProvidentFundBase(providentFundBase);
            vo.setUserId(row.getId());
            vo.setUsername(row.getUsername());
            vo.setProvidentFundCity(city.getName());
            vo.setParticipatingInTheCity(city.getName());
            vo.setIndustrialInjuryRatio(
                    RandomUtil.randomBigDecimal(new BigDecimal("0.5"), new BigDecimal(2)).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            vo.setEnterpriseProportion(RandomUtil.randomBigDecimal(new BigDecimal("5"), new BigDecimal(12)).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            vo.setPersonalProportion(RandomUtil.randomBigDecimal(new BigDecimal("5"), new BigDecimal(12)).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            list.add(vo);
        }
        Resource template = new ClassPathResource("社保数据.xlsx");
        final WriteSheet sheet = EasyExcel.writerSheet().build();
        FileOutputStream f = new FileOutputStream(
                "C:\\Users\\17314\\Desktop\\HRM管理系统\\数据导入\\社保模拟数据.xlsx");
        EasyExcel.write(f, UserSocialSecurityVo.class)
                 .withTemplate(template.getInputStream()).build()
                 .fill(list, sheet)
                 .finish();
        return Result.SUCCESS();
    }

    @GetMapping(value = "citySocial")
    @ApiOperation(value = "随机设置城市社保的缴纳比例")
    public Result user() throws Exception {
//        final List<PaymentItem> all = paymentItemService.findAllPaymentItems();
//        final List<City> all1 = systemFeignClient.findAll().getData();
//        all1.forEach(city -> {
//            final String id = city.getId();
//            all.forEach(baoxian -> {
//                final String name = baoxian.getName();
//                final BigDecimal scaleCompany = baoxian.getScaleCompany();
//                final BigDecimal scalePersonal = baoxian.getScalePersonal();
//                final Boolean switchCompany = baoxian.getSwitchCompany();
//                final Boolean switchPersonal = baoxian.getSwitchPersonal();
//                final CityPaymentItem cityPaymentItem = new CityPaymentItem();
//                cityPaymentItem.setId(IdWorker.getIdStr());
//                cityPaymentItem.setCityId(id);
//                cityPaymentItem.setPaymentItemId(baoxian.getId());
//                cityPaymentItem.setSwitchCompany(switchCompany);
//                cityPaymentItem.setSwitchPersonal(switchPersonal);
//                cityPaymentItem.setScaleCompany(scaleCompany);
//                cityPaymentItem.setScalePersonal(scalePersonal);
//                cityPaymentItem.setName(name);
//                paymentItemService.saveCityPaymentItem(cityPaymentItem);
//            });
//        });
//        return new Result(ResultCode.SUCCESS);
        return null;
    }

    private void buildUserDataExcel() throws IOException {
        List<UserVo> list = new ArrayList<>(50);
        for (int i = 0; i < 25; i++) {
            final UserVo userVo = new UserVo();
            userVo.setUsername("CWCPSYB员工" + (i + 1));
            userVo.setMobile("1868540" + RandomUtil.randomInt(1000, 9999));
            userVo.setDepartmentCode("CWCPSYB");
            userVo.setFormOfEmployment(1);
            userVo.setFormOfManagement(String.valueOf(1));
            userVo.setTimeOfEntry(DateUtil.parseDate("2022-04-04"));
            userVo.setWorkingCity("北京");
            userVo.setWorkNumber(String.valueOf(RandomUtil.randomInt(10000, 99999)));
            list.add(userVo);
        }
        for (int i = 0; i < 25; i++) {
            final UserVo userVo = new UserVo();
            userVo.setUsername("CWCP2员工" + (i + 1));
            userVo.setMobile("1868540" + RandomUtil.randomInt(1000, 9999));
            userVo.setDepartmentCode("CWCP2");
            userVo.setFormOfEmployment(1);
            userVo.setFormOfManagement(String.valueOf(1));
            userVo.setTimeOfEntry(DateUtil.parseDate("2022-04-04"));
            userVo.setWorkingCity("北京");
            userVo.setWorkNumber(String.valueOf(RandomUtil.randomInt(10000, 99999)));
            list.add(userVo);
        }
        Resource template = new ClassPathResource("用户批量添加构造模板.xlsx");
        final WriteSheet sheet = EasyExcel.writerSheet().build();
        FileOutputStream f = new FileOutputStream(new File("D:\\HRM-Managent\\hrm-parent\\hrm-attendance\\src\\main\\resources\\users.xlsx"));
        EasyExcel.write(f, AtteUploadVo.class)
                 .withTemplate(template.getInputStream()).build()
                 .fill(list, sheet)
                 .finish();
    }

    private void buildAttendanceData(int startYear, int endYear, int startMonth, int endMonth, String filename) throws ParseException, IOException {
        List<AtteUploadVo> list = new ArrayList<>();
        String[] timeup = new String[]{"08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00"};
        String[] timedown = new String[]{"17:30:00", "18:00:00", "18:30:00", "19:00:00", "19:30:00"};
        final List<User> users = userDao.findByCompanyId(companyId);
        for (User user : users) {
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
                    System.out.println(m);
                    final String[] monthEveryDay = DateUtils.getMonthEveryDay(m, DatePattern.SIMPLE_MONTH_PATTERN);
                    for (String s : monthEveryDay) {
                        AtteUploadVo atteUploadVo = new AtteUploadVo();
                        atteUploadVo.setAtteDate(s);
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                        final Date parse = simpleDateFormat.parse(s + " " + RandomUtil.randomEle(timeup));
                        final Date parse1 = simpleDateFormat.parse(s + " " + RandomUtil.randomEle(timedown));
                        atteUploadVo.setInTime(parse);
                        atteUploadVo.setOutTime(parse1);
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

    private void generateUserDetailInfo() {
        // 生成用户详细信息
//        final Map map1 = new HashMap<>();
//        map1.put("page", 1);
//        map1.put("size", 10000);
//        final Result all = stemFeignClient.findAll(map1);
//        final PageResult<User> data = (PageResult<User>) all.getData();
//        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
//        for (User row : data.getRows()) {
//            final DateTime birth = RandomUtil.randomDate(new DateTime("1994-01-01"), DateField.YEAR, 0, 8);
//            sourceInfo.setBirthday(DateUtil.format(birth, DatePattern.PURE_DATE_PATTERN));
//            sourceInfo.setUserId(row.getId());
//            sourceInfo.setCompanyId(companyId);
//            sourceInfo.setUsername(row.getUsername());
//            sourceInfo.setDepartmentName(row.getDepartmentName());
//            sourceInfo.setMobile(row.getMobile());
//            final long age = DateUtil.betweenYear(birth, DateUtil.date(), true);
//            sourceInfo.setAge((int) age);
//            sourceInfo.setBankCardNumber(String.valueOf(RandomUtil.randomLong(1000000000000000L, 9999999999999999L)));
//            final Date timeOfEntry = row.getTimeOfEntry();
//            sourceInfo.setTimeOfEntry(s.format(timeOfEntry));
//            sourceInfo.setSex(row.getGender());
//            sourceInfo.setTheHighestDegreeOfEducation(RandomUtil.randomEle(new String[]{"本科", "硕士"}));
//            sourceInfo.setNationalArea("中国大陆");
//            final String idNo = StringUtils.getIdNo(DateUtil.format(birth, DatePattern.PURE_DATE_PATTERN), "1".equals(row.getGender()));
//            sourceInfo.setIdNumber(idNo);
//            sourceInfo.setNativePlace("430000/430100");
//            sourceInfo.setNation(RandomUtil.randomEle(new String[]{"汉族", "布依族", "壮族", "回族", "苗族", "满族"}));
//            sourceInfo.setMaritalStatus(RandomUtil.randomEle(new String[]{"未婚", "已婚"}));
//            sourceInfo.setBloodType(RandomUtil.randomEle(new String[]{"A型", "B型", "AB型", "O型"}));
//            sourceInfo.setDomicile("430000/430100/430103");
//            sourceInfo.setQq(String.valueOf(RandomUtil.randomInt(10000000, 999999999)));
//            sourceInfo.setWechat(sourceInfo.getQq());
//            sourceInfo.setPlaceOfResidence("430000/430100/430103");
//            sourceInfo.setContactTheMobilePhone(row.getMobile());
//            sourceInfo.setPersonalMailbox(sourceInfo.getQq() + "@qq.com");
//            sourceInfo.setSocialSecurityComputerNumber(sourceInfo.getIdNumber());
//            sourceInfo.setProvidentFundAccount(String.valueOf(RandomUtil.randomLong(100000000L, 999999999L)));
//            sourceInfo.setBankCardNumber(BankNumberUtil.getBankNumber(RandomUtil.randomEle(new String[]{"6", "8", "9"})));
//            sourceInfo.setOpeningBank("中国建设银行");
//            sourceInfo.setEducationalType("统招");
//            sourceInfo.setMajor("030000/031600/031601");
//            log.info("{}",sourceInfo);
//            userCompanyPersonalService.save(sourceInfo);
//            redisTemplate.boundHashOps("userDetailList").put(uid, sourceInfo);
//        }
    }
}
