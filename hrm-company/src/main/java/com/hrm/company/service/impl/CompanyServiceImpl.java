package com.hrm.company.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import com.hrm.common.entity.PageResult;
import com.hrm.common.utils.MailUtils;
import com.hrm.company.dao.CompanyDao;
import com.hrm.company.service.CompanyService;
import com.hrm.domain.company.Company;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author LZL
 * @date 2022/1/12-10:17
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyDao companyDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Value("${initial-password}")
    private String initialPassword;

    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    private ThreadPoolExecutor pool =
            new ThreadPoolExecutor(1,
                                   1, 2,
                                   TimeUnit.MINUTES, new LinkedBlockingQueue<>(),
                                   (r) -> new Thread(r, "t1"));

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
        long finalL = l;
        pool.execute(() -> {
            MailUtils.sendMail(company.getMailbox(), "HRM系统账号已成功注册；用户名：" + finalL + "；初始密码：123456", "HRM系统邮件");
        });
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
    public PageResult<Company> findAll(int page, int size) {
        final List<Company> page1 = companyDao.findPage((page - 1) * size, size);
        final int i = companyDao.countOfFindPage();
        return new PageResult<Company>((long) i, page1);
    }

    @Override
    public Company findByManagerId(String id) {
        Company c = companyDao.findByManagerId(id);
        return c;
    }

    @Override
    public List<Company> findByName(String name) {
        List<Company> c = companyDao.findByName(name);
        return c;
    }


}
