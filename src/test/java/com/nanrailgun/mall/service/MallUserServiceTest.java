package com.nanrailgun.mall.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallUserServiceTest {

    @Autowired
    MallUserService mallUserService;

    private static final Logger logger = LoggerFactory.getLogger(MallUserServiceTest.class);

    @Test
    void registerTest() {
        String loginName = "13502694275";
        String password = "123456";
        logger.info("register:{}", mallUserService.register(loginName, password));
    }
}
