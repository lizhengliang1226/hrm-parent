package com.hrm.social;

import com.hrm.common.client.EmployeeFeignClient;
import com.hrm.common.client.SalaryFeignClient;
import com.hrm.common.client.SystemFeignClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * 社保微服务
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/15-9:23
 */
@SpringBootApplication(scanBasePackages = "com.hrm")
@EntityScan(value = "com.hrm.domain.social")
@EnableDiscoveryClient
@EnableFeignClients(clients = {SalaryFeignClient.class, SystemFeignClient.class, EmployeeFeignClient.class})
@Import({OpenEntityManagerInViewFilter.class})
@MapperScan("com.hrm.social.mapper")
public class HrmSocialSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrmSocialSecurityApplication.class);
    }
}
