package com.hrm.employee;


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
@EntityScan("com.hrm.domain.employee")
@EnableDiscoveryClient
@EnableFeignClients(clients = {SystemFeignClient.class})
public class HrmEmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrmEmployeeApplication.class, args);
    }

}