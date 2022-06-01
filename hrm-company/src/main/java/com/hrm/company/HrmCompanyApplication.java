package com.hrm.company;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 企业微服务
 *
 * @author LZL
 * @date 2022/1/12-9:36
 */
@SpringBootApplication(scanBasePackages = "com.hrm")
@EntityScan(value = "com.hrm.domain.company")
@EnableDiscoveryClient
@MapperScan("com.hrm.company.mapper")
public class HrmCompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrmCompanyApplication.class, args);
    }

}
