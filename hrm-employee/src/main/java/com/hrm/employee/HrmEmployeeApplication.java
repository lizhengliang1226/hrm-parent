package com.hrm.employee;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 17314
 */
@SpringBootApplication(scanBasePackages = "com.hrm")
@EntityScan("com.hrm.domain.employee")
@EnableDiscoveryClient
public class HrmEmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrmEmployeeApplication.class, args);
    }

}