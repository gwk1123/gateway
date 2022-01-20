package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 网关 Spring Cloud Gateway 实战负载均衡(Spring Cloud Loadbalancer)
 * https://laker.blog.csdn.net/article/details/112296435?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_paycolumn_v3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_paycolumn_v3&utm_relevant_index=2
 *
 * http://t.zoukankan.com/caohanren-p-13410534.html
 *
 * spring cloud gateway报错java.net.UnknownHostException: 4d59d509898a: Name or service not known
 * https://www.jianshu.com/p/2a2fb061865d
 */

@EnableEurekaClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
