package com.nanrailgun.user_service_provider;

import com.nanrailgun.user_service_provider.service.impl.MallUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceProviderApplicationTests {

    @Autowired
    MallUserServiceImpl mallUserService;

    @Test
    void contextLoads() {
        System.out.println(mallUserService.login("18029967532", "e10adc3949ba59abbe56e057f20f883e"));
    }

    @Test
    void TokenTest() {
        System.out.println(mallUserService.selectByToken("1e2ce0687940f5713101f30ee3750d2-"));
    }

}
