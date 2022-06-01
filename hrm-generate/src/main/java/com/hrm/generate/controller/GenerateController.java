package com.hrm.generate.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hrm.common.client.SystemFeignClient;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.utils.DateUtils;
import com.hrm.domain.attendance.vo.AtteUploadVo;
import com.hrm.domain.employee.vo.UserVo;
import com.hrm.domain.social.vo.UserSocialSecurityVo;
import com.hrm.domain.system.City;
import com.hrm.domain.system.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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
public class GenerateController {
    @Autowired
    private SystemFeignClient systemFeignClient;

    @GetMapping("atteData")
    @ApiOperation(value = "生成数据")
    public Result buildAtteData() throws IOException, ParseException {
        String filename = "C:\\Users\\17314\\Desktop\\HRM管理系统\\数据导入\\2022-3-5.xlsx";
        int startMonth = 3;
        int endMonth = 5;
        int startYear = 2022;
        int endYear = 2022;
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
        String a = "18685404707,李正良,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:31,1500843020646461440,2022-05-06,1,4545|18685405040,CWCPSYB员工1,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:31,1500843020646461440,2022-05-06,1,47001|18685405314,CWCPSYB员工2,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:32,1500843020646461440,2022-05-06,1,54003|18685405092,CWCPSYB员工3,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:32,1500843020646461440,2022-05-06,1,15876|18685401489,CWCPSYB员工4,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:32,1500843020646461440,2022-05-06,1,99291|18685401765,CWCPSYB员工5,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:32,1500843020646461440,2022-05-06,1,17874|18685402076,CWCPSYB员工6,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:33,1500843020646461440,2022-05-06,1,11334|18685403001,CWCPSYB员工7,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:33,1500843020646461440,2022-05-06,1,70771|18685405423,CWCPSYB员工8,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:33,1500843020646461440,2022-05-06,1,96595|18685408673,CWCPSYB员工9,71f939b8a9538aec1ee680b62bf5e2f2c0fcab3bbbc538098b4db918bea7535ff273920a07fef83d,1,2022-05-20 01:30:33,1500843020646461440,2022-05-06,1,24593|18685407642,CWCPSYB员工10,eb70140e615b76edb08f5226585b6de4b1d06916e9052c4e71e9a6f076c3efc7f2e69c825f8a69ec,1,2022-05-20 01:30:33,1500843020646461440,2022-05-06,1,40956|18685403817,CWCPSYB员工11,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:34,1500843020646461440,2022-05-06,1,82266|18685403110,CWCPSYB员工12,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:34,1500843020646461440,2022-05-06,1,38452|18685401015,CWCPSYB员工13,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:34,1500843020646461440,2022-05-06,1,83004|18685409840,CWCPSYB员工14,71f939b8a9538aec1ee680b62bf5e2f2c0fcab3bbbc538098b4db918bea7535ff273920a07fef83d,1,2022-05-20 01:30:34,1500843020646461440,2022-05-06,1,20463|18685402470,CWCPSYB员工15,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:35,1500843020646461440,2022-05-06,1,64139|18685401355,CWCPSYB员工16,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:35,1500843020646461440,2022-05-06,1,67189|18685406982,CWCPSYB员工17,eb70140e615b76edb08f5226585b6de4b1d06916e9052c4e71e9a6f076c3efc7f2e69c825f8a69ec,1,2022-05-20 01:30:35,1500843020646461440,2022-05-06,1,88618|18685407882,CWCPSYB员工18,eb70140e615b76edb08f5226585b6de4b1d06916e9052c4e71e9a6f076c3efc7f2e69c825f8a69ec,1,2022-05-20 01:30:35,1500843020646461440,2022-05-06,1,78589|18685406560,CWCPSYB员工19,eb70140e615b76edb08f5226585b6de4b1d06916e9052c4e71e9a6f076c3efc7f2e69c825f8a69ec,1,2022-05-20 01:30:35,1500843020646461440,2022-05-06,1,63258|18685405806,CWCPSYB员工20,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:36,1500843020646461440,2022-05-06,1,18428|18685403263,CWCPSYB员工21,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:36,1500843020646461440,2022-05-06,1,28304|18685408523,CWCPSYB员工22,71f939b8a9538aec1ee680b62bf5e2f2c0fcab3bbbc538098b4db918bea7535ff273920a07fef83d,1,2022-05-20 01:30:36,1500843020646461440,2022-05-06,1,73934|18685403099,CWCPSYB员工23,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:36,1500843020646461440,2022-05-06,1,93001|18685405648,CWCPSYB员工24,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:37,1500843020646461440,2022-05-06,1,32476|18685405201,CWCPSYB员工25,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:37,1500843020646461440,2022-05-06,1,42179|18685402647,CWCP2员工1,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:37,558842974671323138,2022-05-06,1,55513|18685406938,CWCP2员工2,eb70140e615b76edb08f5226585b6de4b1d06916e9052c4e71e9a6f076c3efc7f2e69c825f8a69ec,1,2022-05-20 01:30:37,558842974671323138,2022-05-06,1,35114|18685405798,CWCP2员工3,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:37,558842974671323138,2022-05-06,1,36453|18685406639,CWCP2员工4,eb70140e615b76edb08f5226585b6de4b1d06916e9052c4e71e9a6f076c3efc7f2e69c825f8a69ec,1,2022-05-20 01:30:38,558842974671323138,2022-05-06,1,17653|18685405281,CWCP2员工5,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:38,558842974671323138,2022-05-06,1,22681|18685403690,CWCP2员工6,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:38,558842974671323138,2022-05-06,1,31984|18685402705,CWCP2员工7,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:38,558842974671323138,2022-05-06,1,62351|18685402471,CWCP2员工8,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:38,558842974671323138,2022-05-06,1,51584|18685401167,CWCP2员工9,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:39,558842974671323138,2022-05-06,1,72078|18685401283,CWCP2员工10,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:39,558842974671323138,2022-05-06,1,67320|18685405892,CWCP2员工11,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:39,558842974671323138,2022-05-06,1,70434|18685408763,CWCP2员工12,71f939b8a9538aec1ee680b62bf5e2f2c0fcab3bbbc538098b4db918bea7535ff273920a07fef83d,1,2022-05-20 01:30:39,558842974671323138,2022-05-06,1,33465|18685401191,CWCP2员工13,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:40,558842974671323138,2022-05-06,1,43626|18685403699,CWCP2员工14,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:40,558842974671323138,2022-05-06,1,69498|18685401511,CWCP2员工15,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:40,558842974671323138,2022-05-06,1,47245|18685402811,CWCP2员工16,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:40,558842974671323138,2022-05-06,1,40727|18685408578,CWCP2员工17,71f939b8a9538aec1ee680b62bf5e2f2c0fcab3bbbc538098b4db918bea7535ff273920a07fef83d,1,2022-05-20 01:30:40,558842974671323138,2022-05-06,1,51029|18685407016,CWCP2员工18,eb70140e615b76edb08f5226585b6de4b1d06916e9052c4e71e9a6f076c3efc7f2e69c825f8a69ec,1,2022-05-20 01:30:41,558842974671323138,2022-05-06,1,52096|18685404338,CWCP2员工19,ea5531e01f7d9ce90c7497dc562cd5720d2a3095b480f9844bfe9f333678e93991b9f379e09a0bb7,1,2022-05-20 01:30:41,558842974671323138,2022-05-06,1,69075|18685403306,CWCP2员工20,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:41,558842974671323138,2022-05-06,1,36686|18685401138,CWCP2员工21,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:41,558842974671323138,2022-05-06,1,34239|18685401135,CWCP2员工22,1c8db647f551fe1d1d9f4aa45811bed477362e903d76bab412122132a046805a8a6ac468506acbcb,1,2022-05-20 01:30:41,558842974671323138,2022-05-06,1,30200|18685403198,CWCP2员工23,7909affbe20a60d67f51df8854401c2dcd4d32b7a33caf58d046d182c3dd36f5e7f2351c2800b295,1,2022-05-20 01:30:42,558842974671323138,2022-05-06,1,58979|18685406272,CWCP2员工24,eb70140e615b76edb08f5226585b6de4b1d06916e9052c4e71e9a6f076c3efc7f2e69c825f8a69ec,1,2022-05-20 01:30:42,558842974671323138,2022-05-06,1,28349|18685407037,CWCP2员工25,eb70140e615b76edb08f5226585b6de4b1d06916e9052c4e71e9a6f076c3efc7f2e69c825f8a69ec,1,2022-05-20 01:30:42,558842974671323138,2022-05-06,1,81974";
        final String[] split = a.split("\\|");
        for (String aaa : split) {
            final String[] split1 = aaa.split(",");
            final String mobile = split1[0];
            final String name = split1[1];
            final String work = split1[8];
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
                        atteUploadVo.setUsername(name);
                        atteUploadVo.setWorkNumber(work);
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
