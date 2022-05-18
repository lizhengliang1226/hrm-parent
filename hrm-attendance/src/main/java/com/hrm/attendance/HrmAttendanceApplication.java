package com.hrm.attendance;


import com.hrm.attendance.client.CompanyFeignClient;
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
@EntityScan(value = "com.hrm.domain.attendance")
@EnableDiscoveryClient
@Import({OpenEntityManagerInViewFilter.class})
@MapperScan("com.hrm.attendance.mapper")
@EnableFeignClients(clients = {CompanyFeignClient.class})
public class HrmAttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrmAttendanceApplication.class);
    }
}
