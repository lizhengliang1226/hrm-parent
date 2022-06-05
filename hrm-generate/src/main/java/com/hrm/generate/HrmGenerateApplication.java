package com.hrm.generate;


import com.hrm.common.client.CompanyFeignClient;
import com.hrm.common.client.SalaryFeignClient;
import com.hrm.common.client.SocialSecurityClient;
import com.hrm.common.client.SystemFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 17314
 */
@SpringBootApplication(scanBasePackages = "com.hrm")
@EntityScan("com.hrm.domain.generate")
@EnableFeignClients(clients = {SalaryFeignClient.class, SystemFeignClient.class, CompanyFeignClient.class, SocialSecurityClient.class})
@EnableDiscoveryClient
public class HrmGenerateApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrmGenerateApplication.class, args);
    }

}