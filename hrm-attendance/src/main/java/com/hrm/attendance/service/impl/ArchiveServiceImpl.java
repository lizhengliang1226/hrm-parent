package com.hrm.attendance.service.impl;

import com.hrm.attendance.client.CompanyFeignClient;
import com.hrm.attendance.dao.ArchiveMonthlyDao;
import com.hrm.attendance.dao.ArchiveMonthlyInfoDao;
import com.hrm.attendance.dao.AttendanceDao;
import com.hrm.attendance.dao.UserDao;
import com.hrm.attendance.service.ArchiveMonthlyInfoServiceImpl;
import com.hrm.attendance.service.ArchiveService;
import com.hrm.domain.attendance.entity.ArchiveMonthly;
import com.hrm.domain.attendance.entity.AttendanceArchiveMonthlyInfo;
import com.hrm.domain.attendance.entity.User;
import com.hrm.domain.attendance.vo.ArchiveMonthlyVO;
import com.hrm.domain.company.Department;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private ArchiveMonthlyDao atteArchiveMonthlyDao;

    @Autowired
    private ArchiveMonthlyInfoDao archiveMonthlyInfoDao;
    @Autowired
    private CompanyFeignClient companyFeignClient;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ArchiveMonthlyInfoServiceImpl amisImpl;

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
            archiveMonthlyInfoDao.deleteByAtteArchiveMonthlyId(id);
            atteArchiveMonthlyDao.delete(am1);
        });
    }

    private void archiveAtteData(String archiveDate, String companyId) {
        //1.查询所有企业用户
        List<User> users = userDao.findByCompanyId(companyId);
        // 获取企业所有部门
        final List<Department> depts = companyFeignClient.findAll().getData().getDepts();
        for (Department dept : depts) {
            ArchiveMonthly archiveMonthly = buildArchiveMonthlyData(archiveDate, companyId, users, dept.getId());
            final List<AttendanceArchiveMonthlyInfo> amiList = buildArchiveMonthlyInfoData(archiveDate, archiveMonthly, dept.getId(), companyId);
            amisImpl.saveBatch(amiList);
            atteArchiveMonthlyDao.save(archiveMonthly);
        }
    }

    private List<AttendanceArchiveMonthlyInfo> buildArchiveMonthlyInfoData(String archiveDate,
                                                                           ArchiveMonthly archiveMonthly,
                                                                           String departmentId,
                                                                           String companyId) {
        List<AttendanceArchiveMonthlyInfo> amiList = new ArrayList<>(64);
        //2.保存归档明细表数据
        final List<User> deptUsers = userDao.findByCompanyIdAndDepartmentId(companyId, departmentId);
        for (User user : deptUsers) {
            AttendanceArchiveMonthlyInfo info = new AttendanceArchiveMonthlyInfo(user);
            //统计每个用户的考勤记录
            Map map = attendanceDao.statisticalByUser(user.getId(), archiveDate + "%");
            info.setSalaryStandards("通用标准");
            info.setStatisData(map);
            info.setId(IdWorker.getIdStr());
            info.setAtteArchiveMonthlyId(archiveMonthly.getId());
            info.setArchiveDate(archiveDate);
            amiList.add(info);
        }
        return amiList;
    }

    private ArchiveMonthly buildArchiveMonthlyData(String archiveDate, String companyId, List<User> users, String departmentId) {
        //1.保存归档主表数据
        ArchiveMonthly archiveMonthly = new ArchiveMonthly();
        archiveMonthly.setId(IdWorker.getIdStr());
        archiveMonthly.setCompanyId(companyId);
        archiveMonthly.setArchiveYear(archiveDate.substring(0, 4));
        archiveMonthly.setArchiveMonth(archiveDate.substring(4));
        archiveMonthly.setDepartmentId(departmentId);
        //总人数
        archiveMonthly.setTotalPeopleNum(users.size());
        // 全勤人数
        final Integer deptFullAtteNum = attendanceDao.getDeptFullAtteNum(companyId, departmentId, archiveDate);
        archiveMonthly.setFullAttePeopleNum(deptFullAtteNum);
        archiveMonthly.setIsArchived(0);
        archiveMonthly.setCreateDate(new Date());
        return archiveMonthly;
    }
}
