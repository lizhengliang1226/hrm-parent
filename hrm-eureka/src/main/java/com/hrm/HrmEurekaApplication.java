package com.hrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author 17314
 */
@SpringBootApplication
@EnableEurekaServer
public class HrmEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrmEurekaApplication.class, args);
    }

}
