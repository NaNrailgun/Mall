package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallGoods;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MallGoodsMapperTest {

    @Resource
    MallGoodsMapper mallGoodsMapper;

    private static final Logger logger = LoggerFactory.getLogger(MallGoodsMapperTest.class);

    @Test
    void selectByPrimaryKey() {
        Long goodsId = 10003L;
        MallGoods mallGoods = mallGoodsMapper.selectByPrimaryKey(goodsId);
        logger.info("goodsId:{}", goodsId);
        logger.info("mallGoods:{}", mallGoods);
    }

    @Test
    void selectByPrimaryKeysTest() {
        List<Long> list = new ArrayList<>();
        for (int i = 10003; i < 10005; i++) {
            list.add((long) i);
        }
        logger.info("list:{}", list);
        List<MallGoods> mallGoodsList = mallGoodsMapper.selectByPrimaryKeys(list);
        logger.info("mallGoods:{}", mallGoodsList);
    }

}
