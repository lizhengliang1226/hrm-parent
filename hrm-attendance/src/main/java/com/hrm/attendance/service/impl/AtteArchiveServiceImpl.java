package com.hrm.attendance.service.impl;


import com.hrm.attendance.dao.ArchiveMonthlyDao;
import com.hrm.attendance.dao.ArchiveMonthlyInfoDao;
import com.hrm.attendance.dao.AttendanceDao;
import com.hrm.attendance.dao.UserDao;
import com.hrm.attendance.service.ArchiveMonthlyInfoServiceImpl;
import com.hrm.attendance.service.ArchiveMonthlyServiceImpl;
import com.hrm.attendance.service.AtteArchiveService;
import com.hrm.attendance.service.AttendanceService;
import com.hrm.common.client.CompanyFeignClient;
import com.hrm.common.entity.PageResult;
import com.hrm.domain.attendance.entity.ArchiveMonthly;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.attendance.vo.ArchiveMonthlyVO;
import com.lzl.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 归档服务实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/16-17:29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AtteArchiveServiceImpl implements AtteArchiveService {

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private ArchiveMonthlyDao atteArchiveMonthlyDao;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private ArchiveMonthlyInfoDao archiveMonthlyInfoDao;
    @Autowired
    private CompanyFeignClient companyFeignClient;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ArchiveMonthlyInfoServiceImpl amisImpl;
    @Autowired
    private ArchiveMonthlyServiceImpl amsImpl;

    @Override
    public void saveArchive(String archiveDate, String companyId) {
        List<ArchiveMonthly> am = atteArchiveMonthlyDao.findByCompanyIdAndArchiveYearAndArchiveMonth(companyId, archiveDate.substring(0, 4),
                                                                                                     archiveDate.substring(4));
        if (am == null || am.size() == 0) {
            archiveAtteData(archiveDate, companyId);
        } else {
            deleteArchiveAtteData(archiveDate, companyId, am);
            archiveAtteData(archiveDate, companyId);
        }
    }

    @Override
    public List<ArchiveMonthlyVO> findAtteHistoryData(String year, String companyId) {
        List<ArchiveMonthlyVO> amv = new ArrayList<>();
        // 查询某年所有的主档
        final List<ArchiveMonthly> aml = atteArchiveMonthlyDao.findByCompanyIdAndArchiveYear(companyId, year);
        for (ArchiveMonthly archiveMonthly : aml) {
            final ArchiveMonthlyVO a = new ArchiveMonthlyVO();
            a.setId(archiveMonthly.getId());
            a.setFullAttendance(archiveMonthly.getFullAttePeopleNum());
            a.setMonth(archiveMonthly.getArchiveMonth());
            a.setTotalNumber(archiveMonthly.getTotalPeopleNum());
            amv.add(a);
        }
        return amv;
    }


    @Override
    public PageResult<AttendanceArchiveMonthlyInfo> findAtteHistoryDetailData(String id, Map map) {
        final int page = (int) map.get("page");
        final int pagesize = (int) map.get("pagesize");
        final Page<AttendanceArchiveMonthlyInfo> atteArchiveMonthlyId =
                archiveMonthlyInfoDao.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                      criteriaBuilder.equal(
                                                              root.get("atteArchiveMonthlyId")
                                                                  .as(String.class), id),
                                              PageRequest.of(page - 1, pagesize));
        final long totalElements = atteArchiveMonthlyId.getTotalElements();
        final List<AttendanceArchiveMonthlyInfo> content = atteArchiveMonthlyId.getContent();
        return new PageResult<>(totalElements, content);
    }

    @Override
    public AttendanceArchiveMonthlyInfo findUserMonthlyDetail(String userId, String yearMonth) {
        final AttendanceArchiveMonthlyInfo byUserIdAndArchiveDate = archiveMonthlyInfoDao.findByUserIdAndArchiveDate(userId, yearMonth);
        return byUserIdAndArchiveDate;
    }

    private void deleteArchiveAtteData(String archiveDate, String companyId, List<ArchiveMonthly> am) {
        am.forEach(am1 -> {
            final String id = am1.getId();
            log.info("{}", id);
            amisImpl.removeByAtteArchiveMonthlyId(id);
            amsImpl.removeById(am1.getId());
        });
    }

    private void archiveAtteData(String archiveDate, String companyId) {
        // 202205
        // 获取所有用户的考勤明细信息
        final PageResult<AttendanceArchiveMonthlyInfo> reports = attendanceService
                .getReports(archiveDate, companyId, 1, 100000);
        ArchiveMonthly a = new ArchiveMonthly();
        a.setId(IdWorker.getIdStr());
        a.setCompanyId(companyId);
        a.setArchiveYear(archiveDate.substring(0, 4));
        a.setArchiveMonth(archiveDate.substring(4));
        a.setUpdateDate(new Date());
        a.setCreateDate(new Date());
        a.setIsArchived(1);
        int fullAtte = 0;
        for (AttendanceArchiveMonthlyInfo info : reports.getRows()) {
            info.setSalaryStandards("通用标准");
            if (info.getIsFullAttendanceint() == 1) {
                fullAtte += 1;
            }
            info.setCreateDate(new Date());
            info.setUpdateDate(new Date());
            info.setSalaryAdjustmentDays("10");
            info.setId(IdWorker.getIdStr());
            info.setAtteArchiveMonthlyId(a.getId());
            info.setArchiveDate(archiveDate);
        }
        a.setFullAttePeopleNum(fullAtte);
        a.setTotalPeopleNum(Integer.parseInt(String.valueOf(reports.getTotal())));
        amsImpl.save(a);
        amisImpl.saveBatch(reports.getRows());
    }
}
