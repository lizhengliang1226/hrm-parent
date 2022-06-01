package com.hrm.social.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.hrm.common.client.SystemFeignClient;
import com.hrm.common.entity.PageResult;
import com.hrm.domain.employee.UserCompanyPersonal;
import com.hrm.domain.social.CityPaymentItem;
import com.hrm.domain.social.UserSocialSecurity;
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
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                                                             r -> new Thread(r, "t1"));
    static ReentrantLock lock = new ReentrantLock();

    @Override
    public PageResult<UserSocialSecuritySimpleVo> findAll(Map map) {
        final Integer page = (Integer) map.get("page");
        final Integer pageSize = (Integer) map.get("pagesize");
        map.put("page", (page - 1) * pageSize);
        final String keyword = (String) map.get("keyword");
        if (keyword != null && keyword.length() > 0) {
            map.put("keyword", "%" + keyword + "%");
        }
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
            final List<CityPaymentItem> byCityId = cityPaymentItemDao.findByCityId(uss.getParticipatingInTheCityId());
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
        cityList = systemFeignClient.findAll().getData();
        final ExcelReaderBuilder read = EasyExcel.read(file.getInputStream(), UserSocialSecurityVo.class, new SocialExcelListener());
        final ExcelReaderSheetBuilder sheet = read.sheet();
        sheet.doRead();
        log.info("社保数据导入完成");
    }

    /**
     * Excel操作的内部类
     */
    class SocialExcelListener extends AnalysisEventListener<UserSocialSecurityVo> {

        @Override
        public void invoke(UserSocialSecurityVo vo, AnalysisContext analysisContext) {
            UserCompanyPersonal userDetailList = (UserCompanyPersonal) redisTemplate.boundHashOps("userDetailList").get(vo.getUserId());
            if (userDetailList == null) {
                userDetailList = new UserCompanyPersonal();
            }
            final UserSocialSecurity us = new UserSocialSecurity();
            for (City city : cityList) {
                if (city.getName().equals(vo.getParticipatingInTheCity())) {
                    vo.setParticipatingInTheCityId(city.getId());
                }
                if (city.getName().equals(vo.getProvidentFundCity())) {
                    vo.setProvidentFundCityId(city.getId());
                }
            }
            vo.setHouseholdRegistration(userDetailList.getDomicile());
            BeanUtils.copyProperties(vo, us);
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
