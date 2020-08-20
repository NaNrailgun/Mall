package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MallUserMapperTest {

    @Resource
    MallUserMapper mallUserMapper;

    private static final Logger logger = LoggerFactory.getLogger(MallUserMapperTest.class);

    @Test
    void insertTest() {
        MallUser mallUser = MallUser.builder()
                .nickName("梅露")
                .isDeleted((byte) 0)
                .lockedFlag((byte) 0)
                .introduceSign("咔噗")
                .loginName("18029967532")
                .passwordMD5("0")
                .build();
        logger.info("MallUser is {}", mallUser);
        logger.info(String.valueOf(mallUserMapper.insert(mallUser)));
    }

    @Test
    void updateByPrimaryKeyTest() {
        MallUser mallUser = MallUser.builder()
                .userId(4L)
                .nickName("梅露")
                .isDeleted((byte) 0)
                .lockedFlag((byte) 0)
                .introduceSign("咔噗")
                .loginName("18029967532")
                .passwordMD5("e10adc3949ba59abbe56e057f20f883e")
                .build();
        logger.info("MallUser is {}", mallUser);
        logger.info(String.valueOf(mallUserMapper.updateByPrimaryKey(mallUser)));
    }

    @Test
    void selectByPrimaryKeyTest() {
        Long userId = 1L;
        MallUser mallUser = mallUserMapper.selectByPrimaryKey(userId);
        logger.info("userId:{},MallUser:{}", userId, mallUser);
    }

    @Test
    void selectByLoginNameTest() {
        String loginName = "18029967532";
        MallUser mallUser = mallUserMapper.selectByLoginName(loginName);
        logger.info("loginName:{},MallUser:{}", loginName, mallUser);
    }
}
