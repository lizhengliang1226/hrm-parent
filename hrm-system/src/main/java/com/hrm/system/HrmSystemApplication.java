package com.hrm.system;


import com.hrm.common.client.CompanyFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * @author LZL
 * 系统微服务启动类
 * @date 2022/3/8-18:40
 * 配置OpenEntityManagerInViewFilter解决no session问题
 */
@SpringBootApplication(scanBasePackages = "com.hrm")
@EntityScan(value = "com.hrm.domain.system")
@EnableDiscoveryClient
@EnableFeignClients(clients = {CompanyFeignClient.class})
@Import({OpenEntityManagerInViewFilter.class})
public class HrmSystemApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext run =
                SpringApplication.run(HrmSystemApplication.class, args);
    }
}
