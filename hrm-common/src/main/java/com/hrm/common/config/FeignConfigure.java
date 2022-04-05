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
 * feign配置
 *
 * @author LZL
 * @date 2022/3/15-0:26
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
                        String value = request.getHeader(name);
                        requestTemplate.header(name, value);
                    }
                }

            }
        };
    }
}
