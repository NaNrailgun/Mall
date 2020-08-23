package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallUserAddress;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class MallUserAddressMapperTest {

    @Resource
    MallUserAddressMapper mallUserAddressMapper;

    private static final Logger logger = LoggerFactory.getLogger(MallUserAddressMapperTest.class);

    @Test
    void insertTest() {
        MallUserAddress userAddress = MallUserAddress.builder()
                .userId(1L)
                .userName("夏哥")
                .userPhone("18029967532")
                .defaultFlag((byte) 0)
                .provinceName("广东")
                .cityName("深圳")
                .regionName("蓝山")
                .detailAddress("hololive事务所")
                .build();
        logger.info("MallUserAddress:{}", userAddress);
        logger.info(String.valueOf(mallUserAddressMapper.insert(userAddress)));
    }

    @Test
    void selectAllAddressTest() {
        Long userId = 1L;
        List<MallUserAddress> list = mallUserAddressMapper.selectAllAddress(userId);
        logger.info("userId:{}", userId);
        logger.info("list:{}", list);
    }

    @Test
    void selectDefaultAddressTest() {
        Long userId = 1L;
        MallUserAddress userAddress = mallUserAddressMapper.selectDefaultAddress(userId);
        logger.info("userId:{}", userId);
        logger.info("MallUserAddress:{}", userAddress);
    }

    @Test
    void selectByPrimaryKeyTest() {
        Long addressId = 2L;
        MallUserAddress userAddress = mallUserAddressMapper.selectByPrimaryKey(addressId);
        logger.info("addressId:{}", addressId);
        logger.info("MallUserAddress:{}", userAddress);
    }

    @Test
    void deleteByPrimaryKeyTest() {
        Long addressId = 2L;
        logger.info("addressId:{}", addressId);
        logger.info(String.valueOf(mallUserAddressMapper.deleteByPrimaryKey(addressId)));
    }

    @Test
    void updateByPrimaryKeyTest() {
        MallUserAddress userAddress = MallUserAddress.builder()
                .addressId(2L)
                .userId(1L)
                .userName("夏哥")
                .userPhone("114514")
                .defaultFlag((byte) 0)
                .isDeleted((byte) 0)
                .provinceName("广东")
                .cityName("深圳")
                .regionName("蓝山")
                .detailAddress("hololive事务所")
                .build();
        logger.info("MallUserAddress:{}", userAddress);
        logger.info(String.valueOf(mallUserAddressMapper.updateByPrimaryKey(userAddress)));
    }
}
