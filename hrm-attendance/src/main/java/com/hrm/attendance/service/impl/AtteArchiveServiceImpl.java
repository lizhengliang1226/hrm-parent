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
import com.hrm.domain.company.Department;
import com.lzl.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public List<ArchiveMonthlyVO> findAtteHistoryData(String departmentId, String year, String companyId) {
        List<ArchiveMonthlyVO> amv = new ArrayList<>();
        List<ArchiveMonthly> list;
        if (departmentId.equals("all")) {
            // 查询全部部门的当月考勤数据
            final List<Department> depts = companyFeignClient.findAll().getData().getDepts();
            depts.forEach(d -> {
                getDeptYearAtteDate(d.getId(), year, companyId, amv);
            });

        } else {
            // 查询某个部门的当月考勤数据
            getDeptYearAtteDate(departmentId, year, companyId, amv);
        }
        return amv;
    }

    private void getDeptYearAtteDate(String departmentId, String year, String companyId, List<ArchiveMonthlyVO> amv) {
        List<ArchiveMonthly> list;
        list = atteArchiveMonthlyDao.findByCompanyIdAndArchiveYearAndDepartmentId(companyId, year, departmentId);
        final Integer num = userDao.countByCompanyIdAndDepartmentId(companyId, departmentId);
        if (list != null && list.size() > 0) {
            list.forEach(am -> {
                String ym = year + am.getArchiveMonth();
                final ArchiveMonthlyVO archiveMonthlyVO = new ArchiveMonthlyVO();
                final Integer deptFullAtteNum = attendanceDao.getDeptFullAtteNum(companyId, departmentId, ym);
                final Integer noClockNum = attendanceDao.getNoClockNum(companyId, ym, departmentId);
                archiveMonthlyVO.setFullAttendance(deptFullAtteNum);
                archiveMonthlyVO.setId(am.getId());
                archiveMonthlyVO.setMonth(am.getArchiveMonth());
                archiveMonthlyVO.setTotalNumber(num);
                archiveMonthlyVO.setNoAttendance(noClockNum);
                amv.add(archiveMonthlyVO);
            });
        }
    }

    @Override
    public List<AttendanceArchiveMonthlyInfo> findAtteHistoryDetailData(String id) {
        return archiveMonthlyInfoDao.findByAtteArchiveMonthlyId(id);
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
        final long l = System.currentTimeMillis();
        // 获取所有用户的考勤明细信息
        final PageResult<AttendanceArchiveMonthlyInfo> reports = attendanceService.getReports(archiveDate, companyId, 1, 100000);
        final long l1 = System.currentTimeMillis();
        log.info("{}", l1 - l);
        // 主档保存列表
        List<ArchiveMonthly> amList = new ArrayList<>();
        // 获取企业所有部门
        final List<Department> depts = companyFeignClient.findAll().getData().getDepts();
        final long l2 = System.currentTimeMillis();
        int fullAtte = 0;
        for (Department dept : depts) {
            // 构建部门主档数据
            ArchiveMonthly archiveMonthly = buildArchiveMonthlyData(archiveDate, companyId, reports.getTotal(), dept.getId());
            // 遍历列表，当该条明细是这个部门的明细时添加信息
            for (AttendanceArchiveMonthlyInfo info : reports.getRows()) {
                if (info.getDepartment().equals(dept.getName())) {
                    info.setSalaryStandards("通用标准");
                    if (info.getIsFullAttendanceint() == 1) {
                        fullAtte += 1;
                    }
                    info.setCreateDate(new Date());
                    info.setUpdateDate(new Date());
                    info.setSalaryAdjustmentDays("10");
                    info.setId(IdWorker.getIdStr());
                    info.setAtteArchiveMonthlyId(archiveMonthly.getId());
                    info.setArchiveDate(archiveDate);
                }
            }
            archiveMonthly.setFullAttePeopleNum(fullAtte);
            fullAtte = 0;
            amList.add(archiveMonthly);
        }
        final long l3 = System.currentTimeMillis();
        log.info("{}", l3 - l2);
        // 批量保存
        amsImpl.saveBatch(amList);
        amisImpl.saveBatch(reports.getRows());
    }

    private ArchiveMonthly buildArchiveMonthlyData(String archiveDate, String companyId, long size, String departmentId) {
        //1.保存归档主表数据
        ArchiveMonthly archiveMonthly = new ArchiveMonthly();
        archiveMonthly.setId(IdWorker.getIdStr());
        archiveMonthly.setCompanyId(companyId);
        archiveMonthly.setArchiveYear(archiveDate.substring(0, 4));
        archiveMonthly.setArchiveMonth(archiveDate.substring(4));
        archiveMonthly.setDepartmentId(departmentId);
        //总人数
        archiveMonthly.setTotalPeopleNum((int) size);
        archiveMonthly.setIsArchived(0);
        archiveMonthly.setCreateDate(new Date());
        return archiveMonthly;
    }
}
