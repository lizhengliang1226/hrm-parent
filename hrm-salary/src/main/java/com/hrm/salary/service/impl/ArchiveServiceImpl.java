package com.hrm.salary.service.impl;

import com.alibaba.fastjson.JSON;
import com.hrm.common.client.AttendanceClient;
import com.hrm.common.client.SocialSecurityClient;
import com.hrm.common.entity.Result;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.salary.SalaryArchive;
import com.hrm.domain.salary.SalaryArchiveDetail;
import com.hrm.domain.salary.Settings;
import com.hrm.domain.social.SocialSecrityArchiveDetail;
import com.hrm.salary.dao.ArchiveDao;
import com.hrm.salary.dao.ArchiveDetailDao;
import com.hrm.salary.dao.SettingsDao;
import com.hrm.salary.dao.UserSalaryDao;
import com.hrm.salary.service.ArchiveService;
import com.hrm.salary.service.SalaryArchiveDetailServiceImpl;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveServiceImpl implements ArchiveService {
    @Autowired
    private ArchiveDao archiveDao;
    @Autowired
    private ArchiveDetailDao archiveDetailDao;
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
        return archiveDao.findByCompanyIdAndYearsMonth(companyId, yearMonth);
    }

    @Override
    public List<SalaryArchiveDetail> findSalaryArchiveDetail(String id) {
        return archiveDetailDao.findByArchiveId(id);
    }

    @Override
    public List<SalaryArchiveDetail> getReports(String yearMonth, String companyId) throws Exception {
        List<SalaryArchiveDetail> list = new ArrayList<>(16);
        final Optional<Settings> byId = set.findById(companyId);
        final Settings settings = byId.orElse(null);
        // 查询企业所有用户
        final List<Map> content = userSalaryDao.findArchivePage(companyId, yearMonth, null).getContent();
        for (Map map : content) {
            SalaryArchiveDetail s = new SalaryArchiveDetail();
            s.setUser(map);
            s.calSalary(settings);
            list.add(s);
        }
        return list;
    }

    @Override
    public void saveArchive(String yearMonth, String companyId) throws Exception {
        SalaryArchive am = archiveDao.findByCompanyIdAndYearsMonth(companyId, yearMonth);
        if (am == null) {
            // 没有数据，构建
            archiveData(yearMonth, companyId);
        } else {
            // 已有数据，先删除在构建
            final String id = am.getId();
            archiveDetailDao.deleteByArchiveId(id);
            archiveDao.deleteById(id);
            archiveData(yearMonth, companyId);
        }
    }

    @Override
    public void newReport(String yearMonth, String companyId) {

    }

    private void archiveData(String yearMonth, String companyId) throws Exception {
        final List<SalaryArchiveDetail> reports = getReports(yearMonth, companyId);
        BigDecimal paybefortax = BigDecimal.ZERO;
        BigDecimal fi = BigDecimal.ZERO;
        final SalaryArchive salaryArchive = new SalaryArchive();
        salaryArchive.setId(IdWorker.getIdStr());
        for (SalaryArchiveDetail report : reports) {
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
        archiveDao.save(salaryArchive);
        salaryArchiveDetailServiceImpl.saveBatch(reports);
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
