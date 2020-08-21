package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallUserToken;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
class MallUserMapperTokenTest {

    @Resource
    MallUserTokenMapper mallUserTokenMapper;

    private static final Logger logger = LoggerFactory.getLogger(MallUserMapperTokenTest.class);

    @Test
    void insertTest() {
        MallUserToken mallUserToken = MallUserToken.builder()
                .userId(1L)
                .token("1")
                .updateTime(new Date())
                .expireTime(new Date())
                .build();
        logger.info("MallUserToken:{}", mallUserToken);
        logger.info(String.valueOf(mallUserTokenMapper.insert(mallUserToken)));
    }

    @Test
    void deleteByPrimaryKey() {
        Long userId = 1L;
        logger.info("userId:{}", userId);
        logger.info(String.valueOf(mallUserTokenMapper.deleteByPrimaryKey(userId)));
    }

    @Test
    void selectByPrimaryKeyTest() {
        Long userId = 1L;
        logger.info("userId:{}", userId);
        logger.info("MallUserToken:{}", mallUserTokenMapper.selectByPrimaryKey(userId));
    }

    @Test
    void selectByTokenTest() {
        String token = "1";
        logger.info("token:{}", token);
        logger.info("MallUserToken:{}", mallUserTokenMapper.selectByToken(token));
    }

    @Test
    void updateByPrimaryKey() {
        MallUserToken mallUserToken = MallUserToken.builder()
                .userId(1L)
                .token("2")
                .updateTime(new Date())
                .expireTime(new Date())
                .build();
        logger.info("MallUserToken:{}", mallUserToken);
        logger.info(String.valueOf(mallUserTokenMapper.updateByPrimaryKey(mallUserToken)));
    }

}
