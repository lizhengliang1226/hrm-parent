package com.hrm.social.service.impl;


import com.hrm.common.client.EmployeeFeignClient;
import com.hrm.common.entity.PageResult;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.social.Archive;
import com.hrm.domain.social.CityPaymentItem;
import com.hrm.domain.social.SocialSecrityArchiveDetail;
import com.hrm.domain.social.enums.CityPaymentItemEnum;
import com.hrm.social.dao.ArchiveDao;
import com.hrm.social.dao.ArchiveDetailDao;
import com.hrm.social.dao.UserSocialSecurityDao;
import com.hrm.social.service.ArchiveService;
import com.hrm.social.service.PaymentItemService;
import com.hrm.social.service.SocialSecurityArchiveDetailServiceImpl;
import com.hrm.social.service.UserSocialService;
import com.lzl.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 社保归档服务实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/15-9:51
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveServiceImpl implements ArchiveService {
    private Map<String, List<CityPaymentItem>> cityPayListMap = new HashMap<>(64);
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Archive findArchive(String companyId, String yearMonth) {
        return archiveDao.findByCompanyIdAndYearsMonth(companyId, yearMonth);
    }

    @Override
    public PageResult<SocialSecrityArchiveDetail> findAllDetailByArchiveId(String id, Integer page, Integer pageSize) {
        final Page<SocialSecrityArchiveDetail> all = archiveDetailDao.findAll(
                (Specification<SocialSecrityArchiveDetail>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get("archiveId").as(String.class), id), PageRequest.of(page - 1, pageSize));
        return new PageResult<>(all.getTotalElements(), all.getContent());
    }

    @Override
    public PageResult<SocialSecrityArchiveDetail> getReports(String yearMonth, String companyId, Integer page, Integer pageSize) throws Exception {
        Page<Map> userSocialSecurityItemPage = null;
        // 根据是否分页查询部分或者全部用户基础信息数据
        if (page == null || pageSize == null) {
            userSocialSecurityItemPage = userSocialSecurityDao.findPage(companyId, null);
        } else {
            userSocialSecurityItemPage = userSocialSecurityDao.findPage(companyId, PageRequest.of(page - 1, pageSize));
        }
        //构建用户的社保列表
        final long start = System.currentTimeMillis();
        List<SocialSecrityArchiveDetail> list = buildSocialSecurityDetailInfo(yearMonth, userSocialSecurityItemPage);
        final long end = System.currentTimeMillis();
        log.info("社保信息归档，报表构建使用时间：{}ms", end - start);
        return new PageResult<>(userSocialSecurityItemPage.getTotalElements(), list);

    }

    private List<SocialSecrityArchiveDetail> buildSocialSecurityDetailInfo(String yearMonth, Page<Map> userSocialSecurityItemPage) {
        List<SocialSecrityArchiveDetail> list = new ArrayList<>();
        for (Map map : userSocialSecurityItemPage.getContent()) {
            String userId = (String) map.get("id");
            String mobile = (String) map.get("mobile");
            String username = (String) map.get("username");
            String departmentName = (String) map.get("departmentName");
            SocialSecrityArchiveDetail vo = new SocialSecrityArchiveDetail(userId, mobile, username, departmentName);
            vo.setTimeOfEntry((Date) map.get("timeOfEntry"));
            vo.setUserCompanyPersonal(map);
            //社保相关信息
            getSocialSecurityData(vo, yearMonth, map);
            list.add(vo);
        }
        return list;
    }


    /**
     * 获取归档信息的社保信息部分
     *
     * @param vo
     * @param yearMonth
     */
    private void getSocialSecurityData(SocialSecrityArchiveDetail vo, String yearMonth, Map map) {
        // 综合
        BigDecimal socialSecurityCompanyPay = BigDecimal.ZERO;
        BigDecimal socialSecurityPersonalPay = BigDecimal.ZERO;
        List<CityPaymentItem> cityPaymentItemList = null;
        cityPaymentItemList = cityPayListMap.get((String) map.get("participatingInTheCityId"));
        if (cityPaymentItemList == null) {
            cityPaymentItemList = (List<CityPaymentItem>) redisTemplate.boundHashOps(SystemConstant.REDIS_CITY_PAYMENT_LIST)
                                                                       .get((String) map.get("participatingInTheCityId"));
            cityPayListMap.put((String) map.get("participatingInTheCityId"), cityPaymentItemList);
        }
        // 查找城市社保项
//        List<CityPaymentItem> cityPaymentItemList = paymentItemService.findAllByCityId((String) map.get("participatingInTheCityId"));

        // 计算各项保险员工和企业需要支付的单项费
        // 养老
        BigDecimal endowmentInsurancePersonal = BigDecimal.ZERO;
        BigDecimal endowmentInsurancePersonalRatio = BigDecimal.ZERO;
        BigDecimal endowmentInsuranceCompany = BigDecimal.ZERO;
        BigDecimal endowmentInsuranceCompanyRatio = BigDecimal.ZERO;
        // 医疗
        BigDecimal medicalInsuranceCompany = BigDecimal.ZERO;
        BigDecimal medicalInsuranceCompanyRatio = BigDecimal.ZERO;
        BigDecimal medicalInsurancePersonal = BigDecimal.ZERO;
        BigDecimal medicalInsurancePersonalRatio = BigDecimal.ZERO;
        // 失业
        BigDecimal unemploymentInsuranceCompany = BigDecimal.ZERO;
        BigDecimal unemploymentInsuranceCompanyRatio = BigDecimal.ZERO;
        BigDecimal unemploymentInsurancePersonal = BigDecimal.ZERO;
        BigDecimal unemploymentInsurancePersonalRatio = BigDecimal.ZERO;
        // 生育
        BigDecimal birthInsuranceCompany = BigDecimal.ZERO;
        BigDecimal birthInsuranceCompanyRatio = BigDecimal.ZERO;
        // 大病
        BigDecimal seriousInsuranceCompany = BigDecimal.ZERO;
        BigDecimal seriousInsuranceCompanyRatio = BigDecimal.ZERO;
        BigDecimal seriousInsurancePersonal = BigDecimal.ZERO;
        BigDecimal seriousInsurancePersonalRatio = BigDecimal.ZERO;
        // 工伤
        BigDecimal injuryInsuranceCompany = BigDecimal.ZERO;
        BigDecimal injuryInsuranceCompanyRatio = BigDecimal.ZERO;
        final BigDecimal socialSecurityBase = new BigDecimal((Integer) map.get("socialSecurityBase"));
        for (CityPaymentItem cityPaymentItem : cityPaymentItemList) {
            BigDecimal augend = BigDecimal.ZERO;
            if (cityPaymentItem.getSwitchCompany()) {
                // 工伤险.因为工伤比例在社保修改页面可以改，所以要单独算
                if (cityPaymentItem.getPaymentItemId()
                                   .equals(CityPaymentItemEnum.INDUCTRIAL_INJURY_INSURANCE.getValue()) && map.get("industrialInjuryRatio") != null) {
                    augend = ((BigDecimal) map.get("industrialInjuryRatio"))
                            .multiply(socialSecurityBase)
                            .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                    injuryInsuranceCompany = augend;
                    injuryInsuranceCompanyRatio = (BigDecimal) map.get("industrialInjuryRatio");

                } else {
                    // 其他的都直接用这个城市要交的比例乘上社保基数
                    if (cityPaymentItem.getPaymentItemId()
                                       .equals(CityPaymentItemEnum.ENDOWMENT_INSURANCE.getValue()) && cityPaymentItem.getScaleCompany() != null) {
                        // 养老保险
                        endowmentInsuranceCompany = augend = cityPaymentItem.getScaleCompany()
                                                                            .multiply(socialSecurityBase)
                                                                            .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                        endowmentInsuranceCompanyRatio = cityPaymentItem.getScaleCompany();
                    }
                    if (cityPaymentItem.getPaymentItemId()
                                       .equals(CityPaymentItemEnum.MEDICAL_INSURANCE.getValue()) && cityPaymentItem.getScaleCompany() != null) {
                        // 医疗保险
                        medicalInsuranceCompany = augend = cityPaymentItem.getScaleCompany()
                                                                          .multiply(socialSecurityBase)
                                                                          .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                        medicalInsuranceCompanyRatio = cityPaymentItem.getScaleCompany();
                    }
                    if (cityPaymentItem.getPaymentItemId()
                                       .equals(CityPaymentItemEnum.UNEMPLOYMENT_INSURANCE.getValue()) && cityPaymentItem.getScaleCompany() != null) {
                        // 失业保险
                        unemploymentInsuranceCompany = augend = cityPaymentItem.getScaleCompany()
                                                                               .multiply(socialSecurityBase)
                                                                               .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                        unemploymentInsuranceCompanyRatio = cityPaymentItem.getScaleCompany();
                    }
                    if (cityPaymentItem.getPaymentItemId()
                                       .equals(CityPaymentItemEnum.BIRTH_INSURANCE.getValue()) && cityPaymentItem.getScaleCompany() != null) {
                        // 生育保险
                        birthInsuranceCompany = augend = cityPaymentItem.getScaleCompany()
                                                                        .multiply(socialSecurityBase)
                                                                        .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                        birthInsuranceCompanyRatio = cityPaymentItem.getScaleCompany();
                    }
                    if (cityPaymentItem.getPaymentItemId()
                                       .equals(CityPaymentItemEnum.SERIOUS_ILLNESS_INSURANCE.getValue()) && cityPaymentItem.getScaleCompany() != null) {
                        // 大病保险
                        seriousInsuranceCompany = augend = cityPaymentItem.getScaleCompany()
                                                                          .multiply(socialSecurityBase)
                                                                          .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                        seriousInsuranceCompanyRatio = cityPaymentItem.getScaleCompany();
                    }

                }

                socialSecurityCompanyPay = socialSecurityCompanyPay.add(augend);
            }
            if (cityPaymentItem.getSwitchPersonal()) {
                // 其他的都直接用这个城市要交的比例乘上社保基数
                if (cityPaymentItem.getPaymentItemId()
                                   .equals(CityPaymentItemEnum.ENDOWMENT_INSURANCE.getValue()) && cityPaymentItem.getScalePersonal() != null) {
                    // 养老保险
                    endowmentInsurancePersonal = augend = cityPaymentItem.getScalePersonal()
                                                                         .multiply(socialSecurityBase)
                                                                         .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                    endowmentInsurancePersonalRatio = cityPaymentItem.getScalePersonal();
                }
                if (cityPaymentItem.getPaymentItemId()
                                   .equals(CityPaymentItemEnum.MEDICAL_INSURANCE.getValue()) && cityPaymentItem.getScalePersonal() != null) {
                    // 医疗保险
                    medicalInsurancePersonal = augend = cityPaymentItem.getScalePersonal()
                                                                       .multiply(socialSecurityBase)
                                                                       .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                    medicalInsurancePersonalRatio = cityPaymentItem.getScalePersonal();
                }
                if (cityPaymentItem.getPaymentItemId()
                                   .equals(CityPaymentItemEnum.UNEMPLOYMENT_INSURANCE.getValue()) && cityPaymentItem.getScalePersonal() != null) {
                    // 失业保险
                    unemploymentInsurancePersonal = augend = cityPaymentItem.getScalePersonal()
                                                                            .multiply(socialSecurityBase)
                                                                            .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                    unemploymentInsurancePersonalRatio = cityPaymentItem.getScalePersonal();
                }
                socialSecurityPersonalPay = socialSecurityPersonalPay.add(augend);
            }
        }
        // 设置单项费用
        vo.setPensionEnterprise(endowmentInsuranceCompany);
        vo.setOldAgeIndividual(endowmentInsurancePersonal);
        vo.setUnemployedEnterprise(unemploymentInsuranceCompany);
        vo.setUnemployedIndividual(unemploymentInsurancePersonal);
        vo.setMedicalEnterprise(medicalInsuranceCompany);
        vo.setMedicalIndividual(medicalInsurancePersonal);
        vo.setIndustrialInjuryEnterprise(injuryInsuranceCompany);
        vo.setChildbearingEnterprise(birthInsuranceCompany);
        vo.setBigDiseaseEnterprise(seriousInsuranceCompany);
        vo.setAPersonOfGreatDisease(seriousInsurancePersonal);
        // 设置单项比例
        vo.setPersonalPensionRatio(endowmentInsurancePersonalRatio);
        vo.setProportionOfPensionEnterprises(endowmentInsuranceCompanyRatio);
        vo.setProportionOfUnemployedEnterprises(unemploymentInsuranceCompanyRatio);
        vo.setPercentageOfUnemployedIndividuals(unemploymentInsurancePersonalRatio);
        vo.setProportionOfMedicalEnterprises(medicalInsuranceCompanyRatio);
        vo.setMedicalPersonalRatio(medicalInsurancePersonalRatio);
        vo.setProportionOfIndustrialInjuryEnterprises(injuryInsuranceCompanyRatio);
        vo.setProportionOfFertilityEnterprises(birthInsuranceCompanyRatio);
        vo.setProportionOfSeriouslyIllEnterprises(seriousInsuranceCompanyRatio);
        vo.setPersonalProportionOfSeriousIllness(seriousInsurancePersonalRatio);
        // 社保合计费用
        vo.setSocialSecurity(socialSecurityCompanyPay.add(socialSecurityPersonalPay));
        // 企业支付费用
        vo.setSocialSecurityEnterprise(socialSecurityCompanyPay);
        // 个人支付
        vo.setSocialSecurityIndividual(socialSecurityPersonalPay);
        // 设置归档信息中的社保信息
        vo.setUserSocialSecurity(map);
        // 设置社保月份
        vo.setSocialSecurityMonth(yearMonth);
        // 设置公积金月份
        vo.setProvidentFundMonth(yearMonth);
    }

    @Override
    public void archive(String yearMonth, String companyId) throws Exception {
        //1.构建归档明细数据
        final long start = System.currentTimeMillis();
        final PageResult<SocialSecrityArchiveDetail> ssad = getReports(yearMonth, companyId, null, null);
        final long end = System.currentTimeMillis();
        log.info("构建报表数据花费时间：{}ms", end - start);
        //1.1 计算当月,企业与员工支出的所有社保金额总和
        BigDecimal enterMoney = BigDecimal.ZERO;
        BigDecimal personMoney = BigDecimal.ZERO;
        //2.查询当月是否已经归档
        Archive archive = this.findArchive(companyId, yearMonth);
        //3.不存在已归档的数据,新建
        if (archive == null) {
            archive = new Archive();
            archive.setCompanyId(companyId);
            archive.setYearsMonth(yearMonth);
            archive.setId(IdWorker.getIdStr());
            archive.setCreationTime(new Date());
        } else {
            // 已经归档过，先删除明细信息，再添加
            archiveDetailDao.deleteByArchiveId(archive.getId());
        }
        // 计算总金额
        for (SocialSecrityArchiveDetail socialSecrityArchiveDetail : ssad.getRows()) {
            BigDecimal t1 = socialSecrityArchiveDetail.getProvidentFundEnterprises() == null ? BigDecimal.ZERO : socialSecrityArchiveDetail.getProvidentFundEnterprises();
            BigDecimal t2 = socialSecrityArchiveDetail.getSocialSecurityEnterprise() == null ? BigDecimal.ZERO : socialSecrityArchiveDetail.getSocialSecurityEnterprise();
            BigDecimal t3 = socialSecrityArchiveDetail.getProvidentFundIndividual() == null ? BigDecimal.ZERO : socialSecrityArchiveDetail.getProvidentFundIndividual();
            BigDecimal t4 = socialSecrityArchiveDetail.getSocialSecurityIndividual() == null ? BigDecimal.ZERO : socialSecrityArchiveDetail.getSocialSecurityIndividual();
            enterMoney = enterMoney.add(t1).add(t2);
            personMoney = enterMoney.add(t3).add(t4);
            socialSecrityArchiveDetail.setId(IdWorker.getIdStr());
            socialSecrityArchiveDetail.setArchiveId(archive.getId());
            socialSecrityArchiveDetail.setYearsMonth(yearMonth);
        }
        // 企业缴纳
        archive.setEnterprisePayment(enterMoney);
        // 个人缴纳
        archive.setPersonalPayment(personMoney);
        // 缴纳总金额
        archive.setTotal(enterMoney.add(personMoney));
        // 保存主档信息
        long st = System.currentTimeMillis();
        archiveDao.save(archive);
        long st1 = System.currentTimeMillis();
        log.info("保存主档时间：{}ms", st1 - st);
        // 保存子档信息
        socialSecurityArchiveDetailService.saveBatch(ssad.getRows());
        long ed = System.currentTimeMillis();
        log.info("保存社保主档和子档花费时间：{}ms", ed - st);
    }

    @Override
    public List<Archive> findArchiveByYear(String year, String companyId) {
        return archiveDao.findByCompanyIdAndYearsMonthLike(companyId, year + "%");
    }

    @Override
    public SocialSecrityArchiveDetail findUserArchiveDetail(String userId, String yearMonth) {
        return archiveDetailDao.findByUserIdAndYearsMonth(userId, yearMonth);
    }

    @Autowired
    private ArchiveDao archiveDao;

    @Autowired
    private ArchiveDetailDao archiveDetailDao;

    @Autowired
    private UserSocialSecurityDao userSocialSecurityDao;

    @Autowired
    private UserSocialService userSocialService;

    @Autowired
    private PaymentItemService paymentItemService;

    @Autowired
    private EmployeeFeignClient employeeFeignClient;
    @Autowired
    private SocialSecurityArchiveDetailServiceImpl socialSecurityArchiveDetailService;

}
