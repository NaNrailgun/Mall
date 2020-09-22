package com.nanrailgun.order_service_provider;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.nanrailgun.order_service_provider.dao")
@EnableAutoDataSourceProxy
public class OrderServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceProviderApplication.class, args);
    }

}
