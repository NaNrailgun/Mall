package com.nanrailgun.mall.service;

import com.nanrailgun.mall.entity.MallUserAddress;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MallUserAddressServiceImplTest {

    @Resource
    MallUserAddressService mallUserAddressService;

    private static final Logger logger = LoggerFactory.getLogger(MallUserAddressServiceImplTest.class);

    @Test
    void getAddressListTest() {
        Long userId = 1L;
        logger.info("userId:{}", userId);
        logger.info("list:{}", mallUserAddressService.getAddressList(userId));
    }

    @Test
    void getAddressByAddressIdTest() {
        Long addressId = 1L;
        MallUserAddress address = mallUserAddressService.getAddressByAddressId(addressId);
        logger.info("addressId:{}", addressId);
        logger.info("MallUserAddress:{}", address);
    }

    @Test
    void getDefaultAddressTest() {
        Long userId = 1L;
        MallUserAddress address = mallUserAddressService.getDefaultAddress(userId);
        logger.info("userId:{}", userId);
        logger.info("MallUserAddress:{}", address);
    }

    @Test
    void saveAddressTest() {
        MallUserAddress userAddress = MallUserAddress.builder()
                .userId(4L)
                .userName("梅露")
                .userPhone("18029967532")
                .defaultFlag((byte) 1)
                .isDeleted((byte) 0)
                .provinceName("广东")
                .cityName("深圳")
                .regionName("蓝山")
                .detailAddress("hololive事务所")
                .build();
        logger.info("MallUserAddress:{}", userAddress);
        mallUserAddressService.saveAddress(userAddress);
    }

    @Test
    void updateAddressTest() {
        MallUserAddress userAddress = MallUserAddress.builder()
                .addressId(4L)
                .userId(4L)
                .userName("梅露")
                .userPhone("180")
                .defaultFlag((byte) 1)
                .isDeleted((byte) 0)
                .provinceName("广东")
                .cityName("深圳")
                .regionName("蓝山")
                .detailAddress("hololive事务所")
                .build();
        logger.info("MallUserAddress:{}", userAddress);
        mallUserAddressService.updateAddress(userAddress);
    }

    @Test
    void deleteAddressTest() {
        Long addressId = 3L;
        logger.info("addressId:{}", addressId);
        logger.info(String.valueOf(mallUserAddressService.deleteAddress(addressId)));
    }
}
