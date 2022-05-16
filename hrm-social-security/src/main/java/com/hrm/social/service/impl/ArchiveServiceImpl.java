package com.hrm.social.service.impl;


import com.alibaba.fastjson.JSON;
import com.hrm.common.entity.Result;
import com.hrm.domain.employee.UserCompanyPersonal;
import com.hrm.domain.social.Archive;
import com.hrm.domain.social.ArchiveDetail;
import com.hrm.domain.social.CityPaymentItem;
import com.hrm.domain.social.UserSocialSecurity;
import com.hrm.social.client.EmployeeFeignClient;
import com.hrm.social.dao.ArchiveDao;
import com.hrm.social.dao.ArchiveDetailDao;
import com.hrm.social.dao.UserSocialSecurityDao;
import com.hrm.social.service.ArchiveService;
import com.hrm.social.service.PaymentItemService;
import com.hrm.social.service.UserSocialService;
import com.lzl.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public Archive findArchive(String companyId, String yearMonth) {
        return archiveDao.findByCompanyIdAndYearsMonth(companyId, yearMonth);
    }

    @Override
    public List<ArchiveDetail> findAllDetailByArchiveId(String id) {
        return archiveDetailDao.findByArchiveId(id);
    }

    @Override
    public List<ArchiveDetail> getReports(String yearMonth, String companyId) throws Exception {
        //查询用户的社保列表 (用户和基本社保数据)
        Page<Map> userSocialSecurityItemPage = userSocialSecurityDao.findPage(companyId, null);
        List<ArchiveDetail> list = new ArrayList<>();
        for (Map map : userSocialSecurityItemPage) {
            String userId = (String) map.get("id");
            log.info(userId);
            String mobile = (String) map.get("mobile");
            String username = (String) map.get("username");
            String departmentName = (String) map.get("departmentName");
            ArchiveDetail vo = new ArchiveDetail(userId, mobile, username, departmentName);
            vo.setTimeOfEntry((Date) map.get("timeOfEntry"));
            //获取个人信息
            Result personalResult = employeeFeignClient.findPersonalInfo(vo.getUserId());
            if (personalResult.isSuccess()) {
                UserCompanyPersonal userCompanyPersonal =
                        JSON.parseObject(
                                JSON.toJSONString(personalResult.getData()),
                                UserCompanyPersonal.class);
                vo.setUserCompanyPersonal(userCompanyPersonal);
            }
            //社保相关信息
            getSocialSecurityData(vo, yearMonth);
            getOtherData(vo, yearMonth);
            list.add(vo);
        }
        return list;

    }

    private void getOtherData(ArchiveDetail vo, String yearMonth) {
        //养老保险、医疗保险、失业保险、工伤保险和生育保险
        /**
         * 1、在职期间交纳了社保养老保险金的职工，在退休之后是可按月领取企业退休职工养老金的。
         *
         * 2、养老金的计算标准为：养老金=基本养老金+个人账户养老金+过渡性养老金。
         *
         * 3、基础养老金=【( c1+A)/2】*n%，其中c1为当地上年度在岗职工月平均工资，A为本人指数化月平均缴费工资，n为退休时的缴费年限。
         *
         * 4、个人账户养老金=个人账户储存额/计发月数，按照这个公式计算出的金额，就是退休后能拿到的个人账户养老金部分。
         *
         * 5、过渡性养老金，依据全省上年度在职职工月平均收入、本人平均缴费指数、创建基本养老保险个人账户前的视作缴费年限来计算。
         */
    }

    /**
     * 获取归档信息的社保信息部分
     *
     * @param vo
     * @param yearMonth
     */
    private void getSocialSecurityData(ArchiveDetail vo, String yearMonth) {
        // 查询用户社保信息
        UserSocialSecurity userSocialSecurity = userSocialService.findById(vo.getUserId());
        if (userSocialSecurity == null) {
            return;
        }
        BigDecimal socialSecurityCompanyPay = new BigDecimal(0);
        BigDecimal socialSecurityPersonalPay = new BigDecimal(0);
        // 查找城市社保项
        List<CityPaymentItem> cityPaymentItemList = paymentItemService.findAllByCityId(userSocialSecurity.getProvidentFundCityId());
        // 计算各项保险员工和企业需要支付的费用
        for (CityPaymentItem cityPaymentItem : cityPaymentItemList) {
            if (cityPaymentItem.getSwitchCompany()) {
                BigDecimal augend;
                // 工伤险
                if (cityPaymentItem.getPaymentItemId().equals("4") && userSocialSecurity.getIndustrialInjuryRatio() != null) {
                    augend = userSocialSecurity.getIndustrialInjuryRatio().multiply(userSocialSecurity.getSocialSecurityBase());
                } else {
                    augend = cityPaymentItem.getScaleCompany().multiply(userSocialSecurity.getSocialSecurityBase());
                }
                BigDecimal divideAugend = augend.divide(new BigDecimal(100));
                socialSecurityCompanyPay = socialSecurityCompanyPay.add(divideAugend);
            }
            if (cityPaymentItem.getSwitchPersonal()) {
                BigDecimal augend = cityPaymentItem.getScalePersonal().multiply(userSocialSecurity.getSocialSecurityBase());
                BigDecimal divideAugend = augend.divide(new BigDecimal(100));
                socialSecurityPersonalPay = socialSecurityPersonalPay.add(divideAugend);
            }
        }
        // 社保合计费用
        vo.setSocialSecurity(socialSecurityCompanyPay.add(socialSecurityPersonalPay));
        // 企业支付费用
        vo.setSocialSecurityEnterprise(socialSecurityCompanyPay);
        // 个人支付
        vo.setSocialSecurityIndividual(socialSecurityPersonalPay);
        // 设置归档信息中的社保信息
        vo.setUserSocialSecurity(userSocialSecurity);
        // 设置社保月份
        vo.setSocialSecurityMonth(yearMonth);
        // 设置公积金月份
        vo.setProvidentFundMonth(yearMonth);
    }

    @Override
    public void archive(String yearMonth, String companyId) throws Exception {
        //1.查询归档明细数据
        List<ArchiveDetail> archiveDetails = getReports(yearMonth, companyId);
        //1.1 计算当月,企业与员工支出的所有社保金额
        BigDecimal enterMoney = new BigDecimal(0);
        BigDecimal personMoney = new BigDecimal(0);
        for (ArchiveDetail archiveDetail : archiveDetails) {
            BigDecimal t1 = archiveDetail.getProvidentFundEnterprises() == null ? new BigDecimal(0) : archiveDetail.getProvidentFundEnterprises();
            BigDecimal t2 = archiveDetail.getSocialSecurityEnterprise() == null ? new BigDecimal(0) : archiveDetail.getSocialSecurityEnterprise();
            BigDecimal t3 = archiveDetail.getProvidentFundIndividual() == null ? new BigDecimal(0) : archiveDetail.getProvidentFundIndividual();
            BigDecimal t4 = archiveDetail.getSocialSecurityIndividual() == null ? new BigDecimal(0) : archiveDetail.getSocialSecurityIndividual();
            enterMoney = enterMoney.add(t1).add(t2);
            personMoney = enterMoney.add(t3).add(t4);
        }
        //2.查询当月是否已经归档
        Archive archive = this.findArchive(companyId, yearMonth);
        //3.不存在已归档的数据,保存
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
        //4.如果存在已归档数据,覆盖
        // 企业缴纳
        archive.setEnterprisePayment(enterMoney);
        // 个人缴纳
        archive.setPersonalPayment(personMoney);
        // 缴纳总金额
        archive.setTotal(enterMoney.add(personMoney));
        archiveDao.save(archive);
        for (ArchiveDetail archiveDetail : archiveDetails) {
            archiveDetail.setId(IdWorker.getIdStr());
            archiveDetail.setArchiveId(archive.getId());
            archiveDetail.setYearsMonth(yearMonth);
            archiveDetailDao.save(archiveDetail);
        }
    }

    @Override
    public List<Archive> findArchiveByYear(String year, String companyId) {
        return archiveDao.findByCompanyIdAndYearsMonthLike(companyId, year + "%");
    }

    @Override
    public ArchiveDetail findUserArchiveDetail(String userId, String yearMonth) {
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

}
