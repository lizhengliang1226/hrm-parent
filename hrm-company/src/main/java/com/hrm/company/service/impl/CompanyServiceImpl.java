package com.hrm.company.service.impl;

import com.hrm.company.dao.CompanyDao;
import com.hrm.company.service.CompanyService;
import com.hrm.domain.company.Company;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LZL
 * @date 2022/1/12-10:17
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyDao companyDao;


    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }


    @Override
    public void add(Company company) {
        //设置id
        String id = IdWorker.getIdStr();
        company.setId(id);
        //设置默认状态
        //审核状态 1-已审核 0-未审核
        //公司状态 1-可用 0-不可用
        company.setAuditState("0");
        company.setState(1);
        //保存
        companyDao.save(company);

    }

    @Override
    public void delete(String id) {
        companyDao.deleteById(id);

    }

    @Override
    public void update(Company company) {
        Company temp = companyDao.findById(company.getId()).get();
        temp.setName(company.getName());
        temp.setBalance(company.getBalance());
        companyDao.save(temp);

    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id).get();
    }

    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }


}
