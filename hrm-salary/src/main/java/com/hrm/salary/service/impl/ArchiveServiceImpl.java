package com.hrm.salary.service.impl;

import com.alibaba.fastjson.JSON;
import com.hrm.common.client.AttendanceClient;
import com.hrm.common.client.SocialSecurityClient;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.salary.SalaryArchive;
import com.hrm.domain.salary.SalaryArchiveDetail;
import com.hrm.domain.salary.Settings;
import com.hrm.domain.social.SocialSecrityArchiveDetail;
import com.hrm.salary.dao.SalaryArchiveDao;
import com.hrm.salary.dao.SalaryArchiveDetailDao;
import com.hrm.salary.dao.SettingsDao;
import com.hrm.salary.dao.UserSalaryDao;
import com.hrm.salary.service.ArchiveService;
import com.hrm.salary.service.SalaryArchiveDetailServiceImpl;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveServiceImpl implements ArchiveService {
    @Autowired
    private SalaryArchiveDao salaryArchiveDao;
    @Autowired
    private SalaryArchiveDetailDao salaryArchiveDetailDao;
    @Autowired
    private SettingsDao set;
    @Autowired
    private AttendanceClient attendanceFeignClient;
    @Autowired
    private UserSalaryDao userSalaryDao;
    @Autowired
    private SocialSecurityClient socialFeignClient;
    @Autowired
    SalaryArchiveDetailServiceImpl salaryArchiveDetailServiceImpl;

    @Override
    public SalaryArchive findSalaryArchive(String companyId, String yearMonth) {
        return salaryArchiveDao.findByCompanyIdAndYearsMonth(companyId, yearMonth);
    }

    @Override
    public PageResult<SalaryArchiveDetail> findSalaryArchiveDetail(String id, Map map) {
        final int page = (int) map.get("page");
        final int pagesize = (int) map.get("pagesize");
        final Page<SalaryArchiveDetail> sadList =
                salaryArchiveDetailDao.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                       criteriaBuilder.equal(
                                                               root.get("archiveId")
                                                                   .as(String.class), id),
                                               PageRequest.of(page - 1, pagesize));
        final long totalElements = sadList.getTotalElements();
        final List<SalaryArchiveDetail> content = sadList.getContent();
        return new PageResult<>(totalElements, content);

    }

    @Override
    public PageResult<SalaryArchiveDetail> getReports(String yearMonth, String companyId, int page, int pagesize) throws Exception {
        List<SalaryArchiveDetail> list = new ArrayList<>(16);
        final Optional<Settings> byId = set.findById(companyId);
        final Settings settings = byId.orElse(null);
        // 查询企业所有用户
        final Page<Map> archivePage = userSalaryDao.findArchivePage(companyId, yearMonth, PageRequest.of(page - 1, pagesize));
        for (Map map : archivePage.getContent()) {
            SalaryArchiveDetail s = new SalaryArchiveDetail();
            s.setUser(map);
            s.calSalary(settings);
            list.add(s);
        }
        return new PageResult<>(archivePage.getTotalElements(), list);
    }

    @Override
    public void saveArchive(String yearMonth, String companyId) throws Exception {
        SalaryArchive am = salaryArchiveDao.findByCompanyIdAndYearsMonth(companyId, yearMonth);
        if (am == null) {
            // 没有数据，构建
            archiveData(yearMonth, companyId);
        } else {
            // 已有数据，先删除在构建
            final String id = am.getId();
            salaryArchiveDetailDao.deleteByArchiveId(id);
            salaryArchiveDao.deleteById(id);
            archiveData(yearMonth, companyId);
        }
    }

    @Override
    public void newReport(String yearMonth, String companyId) {

    }

    @Override
    public List<SalaryArchive> findAllSalaryArchive(String companyId, String year) {
        final List<SalaryArchive> saList = salaryArchiveDao.findByCompanyIdAndYearsMonthLike(companyId,
                                                                                             year.substring(0, 4) + "%");

        return saList;
    }

    private void archiveData(String yearMonth, String companyId) throws Exception {
        final PageResult<SalaryArchiveDetail> reports = getReports(yearMonth, companyId, 1, 10000);

        BigDecimal paybefortax = BigDecimal.ZERO;
        BigDecimal fi = BigDecimal.ZERO;
        final SalaryArchive salaryArchive = new SalaryArchive();
        salaryArchive.setId(IdWorker.getIdStr());
        for (SalaryArchiveDetail report : reports.getRows()) {
            paybefortax = paybefortax.add(report.getPaymentBeforeTax());
            fi = fi.add(report.getSocialSecurityProvidentFundEnterprises());
            report.setArchiveId(salaryArchive.getId());
            report.setId(IdWorker.getIdStr());
        }
        salaryArchive.setYearsMonth(yearMonth);
        salaryArchive.setCompanyId(companyId);
        salaryArchive.setGrossSalary(paybefortax);
        salaryArchive.setFiveInsurances(fi);
        salaryArchive.setCreationTime(new Date());
        salaryArchiveDao.save(salaryArchive);
        salaryArchiveDetailServiceImpl.saveBatch(reports.getRows());
    }

    /**
     * 考勤归档明细获取
     *
     * @param userId
     * @param yearMonth
     * @return
     * @throws Exception
     */
    private AttendanceArchiveMonthlyInfo getAtteInfo(String userId, String yearMonth) throws Exception {
        Result result = attendanceFeignClient.userAtteHistoryArchiveDetailData(userId, yearMonth);
        AttendanceArchiveMonthlyInfo info = null;
        if (result.isSuccess()) {
            info = JSON.parseObject(JSON.toJSONString(result.getData()), AttendanceArchiveMonthlyInfo.class);
        }
        return info;
    }

    /**
     * 社保归档明细获取
     *
     * @param userId
     * @param yearMonth
     * @return
     * @throws Exception
     */
    private SocialSecrityArchiveDetail getSocialInfo(String userId, String yearMonth) throws Exception {
        Map map = new HashMap(2);
        map.put("userId", userId);
        map.put("yearMonth", yearMonth);
        Result result = socialFeignClient.userHistoryArchiveData(map);
        SocialSecrityArchiveDetail info = null;
        if (result.isSuccess()) {
            info = JSON.parseObject(JSON.toJSONString(result.getData()), SocialSecrityArchiveDetail.class);
        }
        return info;
    }
}
