package com.hrm.employee.service.impl;

import com.hrm.common.service.BaseService;
import com.hrm.common.utils.IdWorker;
import com.hrm.domain.employee.EmployeeArchive;
import com.hrm.employee.dao.ArchiveDao;
import com.hrm.employee.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class ArchiveServiceImpl extends BaseService implements ArchiveService {
    @Autowired
    private ArchiveDao archiveDao;
    @Autowired
    private IdWorker idWorker;

    @Override
    public void save(EmployeeArchive archive) {
        archive.setId(idWorker.nextId() + "");
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
    public Page<EmployeeArchive> findSearch(Map<String, Object> map, int page, int size) {
        return archiveDao.findAll(createSpecification(map), PageRequest.of(page - 1, size));
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
            if (searchMap.get("companyId") != null && !"".equals(searchMap.get("companyId"))) {
                predicateList.add(cb.like(root.get("companyId").as(String.class), (String) searchMap.get("companyId")));
            }
            if (searchMap.get("year") != null && !"".equals(searchMap.get("year"))) {
                predicateList.add(cb.like(root.get("month").as(String.class), (String) searchMap.get("year")));
            }
            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
    }
}
