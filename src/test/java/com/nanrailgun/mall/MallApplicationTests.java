package com.nanrailgun.mall;

import com.nanrailgun.springbootstartersnowflake.HelloworldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallApplicationTests {

    @Autowired
    HelloworldService helloworldService;

    @Test
    void contextLoads() {
        System.out.println(helloworldService.sayHello());
    }

}
