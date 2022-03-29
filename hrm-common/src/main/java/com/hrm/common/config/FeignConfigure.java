package com.hrm.common.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Description feign配置
 * @Author LZL
 * @Date 2022/3/15-0:26
 */
@Configuration
@Slf4j
public class FeignConfigure {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                final HttpServletRequest request = requestAttributes.getRequest();
                final Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while(headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        log.info("name: {}", name);
                        String value = request.getHeader(name);
                        log.info("value: {}", value);
                        requestTemplate.header(name, value);
                    }
                }

            }
        };
    }
}
