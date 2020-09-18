package com.nanrailgun.goods_service_provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nanrailgun.goods_service_provider.dao")
@EnableDubbo
public class GoodsServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsServiceProviderApplication.class, args);
    }

}
