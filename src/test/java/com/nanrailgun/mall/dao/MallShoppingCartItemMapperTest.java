package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallShoppingCartItem;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MallShoppingCartItemMapperTest {

    @Resource
    MallShoppingCartItemMapper mallShoppingCartItemMapper;

    private static final Logger logger = LoggerFactory.getLogger(MallShoppingCartItemMapperTest.class);

    @Test
    void insertTest() {
        MallShoppingCartItem mallShoppingCartItem = new MallShoppingCartItem();
        mallShoppingCartItem.setGoodsId(10005L);
        mallShoppingCartItem.setGoodsCount(3);
        mallShoppingCartItem.setUserId(4L);
        logger.info("mallShoppingCartItem:{}", mallShoppingCartItem);
        logger.info("result:{}", mallShoppingCartItemMapper.insert(mallShoppingCartItem));
    }

    @Test
    void selectByPrimaryKeyTest() {
        Long id = 3L;
        MallShoppingCartItem item = mallShoppingCartItemMapper.selectByPrimaryKey(id);
        logger.info("id:{}", id);
        logger.info("item:{}", item);
    }

    @Test
    void selectByUserIdAndGoodsIdTest() {
        Long userId = 4L;
        Long goodsId = 10006L;
        MallShoppingCartItem item = mallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, goodsId);
        logger.info("userId:{},goodsId:{}", userId, goodsId);
        logger.info("item:{}", item);
    }

    @Test
    void selectByUserIdTest() {
        Long userId = 3L;
        List<MallShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.selectByUserId(userId);
        logger.info("userId:{}", userId);
        logger.info("list:{}", mallShoppingCartItems);
    }

    @Test
    void selectCountByUserIdTest() {
        Long userId = 3L;
        int num = mallShoppingCartItemMapper.selectCountByUserId(userId);
        logger.info("userId:{}", userId);
        logger.info("num:{}", num);
    }

    @Test
    void updateByPrimaryKeyTest() {
        MallShoppingCartItem mallShoppingCartItem = new MallShoppingCartItem();
        mallShoppingCartItem.setCartItemId(1L);
        mallShoppingCartItem.setGoodsCount(4);
        logger.info("mallShoppingCartItem:{}", mallShoppingCartItem);
        logger.info("result:{}", mallShoppingCartItemMapper.updateByPrimaryKey(mallShoppingCartItem));
    }

    @Test
    void deleteByPrimaryTest() {
        logger.info("result:{}", mallShoppingCartItemMapper.deleteByPrimary(1L));
    }

    @Test
    void deleteBatchTest() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        logger.info("result:{}", mallShoppingCartItemMapper.deleteBatch(list));
    }

}
