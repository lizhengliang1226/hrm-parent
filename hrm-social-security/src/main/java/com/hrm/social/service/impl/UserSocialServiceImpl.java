package com.hrm.social.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.fastjson.JSON;
import com.hrm.common.client.SystemFeignClient;
import com.hrm.common.entity.PageResult;
import com.hrm.common.utils.PageUtils;
import com.hrm.domain.attendance.entity.User;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.social.CityPaymentItem;
import com.hrm.domain.social.UserSocialSecurity;
import com.hrm.domain.social.enums.UserSocialEnum;
import com.hrm.domain.social.vo.UserSocialSecuritySimpleVo;
import com.hrm.domain.social.vo.UserSocialSecurityVo;
import com.hrm.domain.system.City;
import com.hrm.social.dao.CityPaymentItemDao;
import com.hrm.social.dao.UserSocialSecurityDao;
import com.hrm.social.mapper.UserSocialSecurityMapper;
import com.hrm.social.service.UserSocialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 企业社保缴纳设置实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/15-9:55
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserSocialServiceImpl implements UserSocialService {
    @Autowired
    private UserSocialSecurityMapper userSocialSecurityMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserSocialSecurityDao userSocialSecurityDao;
    @Autowired
    private CityPaymentItemDao cityPaymentItemDao;
    @Autowired
    private SystemFeignClient systemFeignClient;
    private List<City> cityList = new ArrayList<>();
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                                                             r -> new Thread(r, "t1"));
    static ReentrantLock lock = new ReentrantLock();
    private Map<String, List<CityPaymentItem>> cityPayList = new HashMap<>();
    @Override
    public PageResult<UserSocialSecuritySimpleVo> findAll(Map map) {
        PageUtils.doPage(map);
        final List<UserSocialSecuritySimpleVo> byConditions = userSocialSecurityMapper.findByConditions(map);
        final Integer count = userSocialSecurityMapper.count(map);
        final PageResult<UserSocialSecuritySimpleVo> pr = new PageResult<>();
        pr.setTotal(Long.valueOf(count));
        pr.setRows(byConditions);
        return pr;
    }

    @Override
    public UserSocialSecurity findById(String id) {
        Optional<UserSocialSecurity> optional = userSocialSecurityDao.findById(id);
        return optional.orElse(new UserSocialSecurity());
    }

    @Override
    public void save(UserSocialSecurity uss) {
        lock.lock();
        try {
            List<CityPaymentItem> byCityId = null;
            byCityId = cityPayList.get(uss.getParticipatingInTheCityId());
            if (byCityId == null) {
                byCityId = cityPaymentItemDao.findByCityId(uss.getParticipatingInTheCityId());
                cityPayList.put(uss.getParticipatingInTheCityId(), byCityId);
            }
            BigDecimal comTol = new BigDecimal(0);
            BigDecimal personalTol = new BigDecimal(0);
            DecimalFormat df = new DecimalFormat("#.00");
            // 计算企业和个人缴纳的社保费用
            for (CityPaymentItem item : byCityId) {
                if ("工伤".equals(item.getName()) && item.getSwitchCompany()) {
                    item.setScaleCompany(uss.getIndustrialInjuryRatio());
                }
                if (item.getSwitchCompany()) {
                    final BigDecimal divide = uss.getSocialSecurityBase().multiply(item.getScaleCompany()).divide(new BigDecimal(100));
                    final String format = df.format(divide);
                    final BigDecimal bigDecimal = new BigDecimal(format);
                    comTol = comTol.add(bigDecimal);
                }
                if (item.getSwitchPersonal()) {
                    final BigDecimal divide = uss.getSocialSecurityBase()
                                                 .multiply(item.getScalePersonal())
                                                 .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN);
                    final String format = df.format(divide);
                    final BigDecimal bigDecimal = new BigDecimal(format);
                    personalTol = personalTol.add(bigDecimal);
                }
            }
            // 计算公积金费用
            final BigDecimal comFund = uss.getEnterpriseProportion().multiply(uss.getProvidentFundBase()).divide(new BigDecimal(100));
            final String format = df.format(comFund);
            // 格式化后企业缴纳公积金
            final BigDecimal com = new BigDecimal(format);
            // 个人缴纳公积金
            final BigDecimal personalFund = uss.getPersonalProportion().multiply(uss.getProvidentFundBase()).divide(new BigDecimal(100));
            final String format1 = df.format(personalFund);
            final BigDecimal person = new BigDecimal(format1);
            uss.setEnterpriseProvidentFundPayment(com);
            uss.setPersonalProvidentFundPayment(person);
            uss.setSocialSecurityCompanyBase(comTol);
            uss.setSocialSecurityPersonalBase(personalTol);
            uss.setLastModifyTime(new Date());
            uss.setSocialSecuritySwitchUpdateTime(new Date());
            uss.setProvidentFundSwitchUpdateTime(new Date());
            userSocialSecurityDao.save(uss);
        } finally {
            lock.unlock();
        }


    }

    @Override
    public void importSocialExcel(MultipartFile file, String companyId) throws IOException {
        log.info("社保数据开始导入");
        cityList = systemFeignClient.findCityList().getData();
        final ExcelReaderBuilder read = EasyExcel.read(file.getInputStream(),
                                                       UserSocialSecurityVo.class, new SocialExcelListener());
        final ExcelReaderSheetBuilder sheet = read.sheet();
        sheet.doRead();
        log.info("社保数据导入完成");
    }

    /**
     * 社保信息导入Excel操作的内部类
     */
    class SocialExcelListener extends AnalysisEventListener<UserSocialSecurityVo> {

        @Override
        public void invoke(UserSocialSecurityVo vo, AnalysisContext analysisContext) {
            final String mobile = vo.getMobile();
//            User user = SystemCache.USER_INFO_CACHE.get(mobile);
//            if(user==null){
            final Object o = redisTemplate.boundHashOps(SystemConstant.REDIS_USER_LIST).get(vo.getMobile());
            User user = JSON.parseObject(JSON.toJSONString(o), User.class);
            // 存入本地缓存
//                SystemCache.USER_INFO_CACHE.put(vo.getMobile(), user);
//            }
            vo.setUserId(user.getId());
            final UserSocialSecurity us = new UserSocialSecurity();
            for (City city : cityList) {
                if (city.getName().equals(vo.getParticipatingInTheCity())) {
                    vo.setParticipatingInTheCityId(city.getId());
                }
                if (city.getName().equals(vo.getProvidentFundCity())) {
                    vo.setProvidentFundCityId(city.getId());
                }
            }
            BeanUtils.copyProperties(vo, us);
            final String socialSecurityType = vo.getSocialSecurityType();
            final String householdRegistrationType = vo.getHouseholdRegistrationType();
            final String st = UserSocialEnum.lookup(socialSecurityType).getValue();
            final String ht = UserSocialEnum.lookup(householdRegistrationType).getValue();
            us.setSocialSecurityType(Integer.valueOf(st));
            us.setHouseholdRegistrationType(Integer.valueOf(ht));
            // 默认缴纳公积金和社保
            us.setEnterprisesPaySocialSecurityThisMonth(1);
            us.setEnterprisesPayTheProvidentFundThisMonth(1);
            pool.execute(() -> {
                save(us);
            });
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

    }


}
