package com.hrm.employee.service.impl;

import com.hrm.common.service.BaseSpecService;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.employee.EmployeeArchive;
import com.hrm.employee.dao.ArchiveDao;
import com.hrm.employee.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 17314
 */
@Service
public class ArchiveServiceImpl extends BaseSpecService<EmployeeArchive> implements ArchiveService {
    @Autowired
    private ArchiveDao archiveDao;


    @Override
    public void save(EmployeeArchive archive) {
        archive.setCreateTime(new Date());
        archiveDao.save(archive);
    }

    @Override
    public EmployeeArchive findLast(String companyId, String month) {
        EmployeeArchive archive = archiveDao.findByLast(companyId, month);
        return archive;
    }

    @Override
    public List<EmployeeArchive> findAll(Integer page, Integer pagesize, String year, String companyId) {
        int index = (page - 1) * pagesize;
        return archiveDao.findAllData(companyId, year + "%", index, pagesize);
    }

    @Override
    public Long countAll(String year, String companyId) {
        return archiveDao.countAllData(companyId, year + "%");
    }


    @Override
    public List<EmployeeArchive> findSearch(String year, String companyId) {
        return archiveDao.findByCompanyIdAndMonthLike(companyId, year);
    }

    @Override
    public EmployeeArchive findByMonth(String month1) {
        return archiveDao.findByMonth(month1);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<EmployeeArchive> createSpecification(Map searchMap) {
        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<Predicate>();
            // 企业id
            if (searchMap.get(SystemConstant.COMPANY_ID) != null && !"".equals(searchMap.get(SystemConstant.COMPANY_ID))) {
                predicateList.add(cb.equal(root.get(SystemConstant.COMPANY_ID).as(String.class), (String) searchMap.get(SystemConstant.COMPANY_ID)));
            }
            if (searchMap.get(SystemConstant.YEAR) != null && !"".equals(searchMap.get(SystemConstant.YEAR))) {
                predicateList.add(cb.like(root.get(SystemConstant.MONTH).as(String.class), ((String) searchMap.get(SystemConstant.YEAR))));
            }
            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
