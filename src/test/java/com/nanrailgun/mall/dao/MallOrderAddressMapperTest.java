package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallOrderAddress;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MallOrderAddressMapperTest {

    @Resource
    MallOrderAddressMapper mallOrderAddressMapper;

    private static final Logger logger = LoggerFactory.getLogger(MallOrderAddressMapperTest.class);

    @Test
    void insert() {
        MallOrderAddress address = new MallOrderAddress();
        address.setOrderId(1L);
        address.setCityName("上海");
        address.setDetailAddress("hololive");
        int result = mallOrderAddressMapper.insert(address);
        logger.info("address:{}", address);
        logger.info("result:{}", result);
    }
}
