package com.hrm.company.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import com.hrm.common.utils.MailUtils;
import com.hrm.company.dao.CompanyDao;
import com.hrm.company.service.CompanyService;
import com.hrm.domain.company.Company;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author LZL
 * @date 2022/1/12-10:17
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyDao companyDao;

    @Value("${initial-password}")
    private String initialPassword;

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
        long l;
        do {
            l = RandomUtil.randomLong(10000000000L, 99999999999L);
            final Company byManagerId = companyDao.findByManagerId(String.valueOf(l));
            if (byManagerId == null) {
                break;
            }
        } while (true);
        MailUtils.sendMail(company.getMailbox(), "HRM系统账号已成功注册；用户名：" + l + "；初始密码：123456", "HRM系统邮件");
        DES des = SecureUtil.des(String.valueOf(l).getBytes(StandardCharsets.UTF_8));
        final String s = des.encryptHex(initialPassword);

        company.setPassword(s);
        company.setManagerId(String.valueOf(l));
        company.setCreateTime(new Date());
        //保存
        companyDao.save(company);
    }

    @Override
    public void delete(String id) {
        companyDao.deleteById(id);

    }

    @Override
    public void update(Company company) {
        companyDao.save(company);
    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id).get();
    }

    @Override
    public Page<Company> findAll(int page, int size) {
        final Page<Company> all = companyDao.findAll(PageRequest.of(page - 1, size));
        return all;
    }

    @Override
    public Company findByManagerId(String id) {
        Company c = companyDao.findByManagerId(id);
        return c;
    }


}
