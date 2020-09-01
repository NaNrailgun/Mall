package com.nanrailgun.mall.service;

import com.nanrailgun.mall.controller.vo.MallShoppingCartItemVO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MallShoppingCartItemServiceTest {

    @Autowired
    MallShoppingCartItemService mallShoppingCartItemService;

    private static final Logger logger = LoggerFactory.getLogger(MallShoppingCartItemServiceTest.class);

    @Test
    void getAllShoppingCartItemTest() {
        Long userId = 4L;
        List<MallShoppingCartItemVO> list = mallShoppingCartItemService.getAllShoppingCartItem(userId);
        logger.info("userId:{}", userId);
        logger.info("list:{}", list);
    }
}
