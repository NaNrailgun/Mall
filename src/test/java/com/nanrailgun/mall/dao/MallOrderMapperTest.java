package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallOrder;
import com.nanrailgun.mall.utils.NumberUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MallOrderMapperTest {

    @Resource
    MallOrderMapper mallOrderMapper;

    private static final Logger logger = LoggerFactory.getLogger(MallOrderMapperTest.class);

    @Test
    void insertTest() {
        MallOrder order = new MallOrder();
        order.setOrderNo(String.valueOf(NumberUtil.genRandomNumber(4)));
        order.setUserId(4L);
        order.setTotalPrice(1);
        int result = mallOrderMapper.insert(order);
        logger.info("order:{}", order);
        logger.info("result:{}", result);
    }

    @Test
    void updateByPrimaryKeyTest() {
        MallOrder order = new MallOrder();
        order.setOrderId(1L);
        order.setOrderNo(String.valueOf(NumberUtil.genRandomNumber(4)));
        order.setUserId(4L);
        order.setTotalPrice(100);
        int result = mallOrderMapper.updateByPrimaryKey(order);
        logger.info("order:{}", order);
        logger.info("result:{}", result);
    }

    @Test
    void closeOrderTest() {
        Long orderId = 1L;
        int status = -1;
        logger.info(String.valueOf(mallOrderMapper.closeOrder(orderId, status)));
    }

    @Test
    void selectByOrderNoTest() {
        logger.info("order:{}", mallOrderMapper.selectByOrderNo("7389"));
    }
}
