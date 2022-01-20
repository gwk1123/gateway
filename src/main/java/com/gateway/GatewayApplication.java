package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * http://t.zoukankan.com/caohanren-p-13410534.html
 *
 * https://www.jianshu.com/p/2a2fb061865d  spring cloud gateway报错java.net.UnknownHostException: 4d59d509898a: Name or service not known
 */

@EnableEurekaClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
