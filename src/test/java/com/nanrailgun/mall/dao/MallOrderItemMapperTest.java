package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallOrderItem;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MallOrderItemMapperTest {

    @Resource
    MallOrderItemMapper mallOrderItemMapper;

    private static final Logger logger = LoggerFactory.getLogger(MallOrderItemMapperTest.class);

    @Test
    void insertBatchTest() {
        List<MallOrderItem> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MallOrderItem mallOrderItem = new MallOrderItem();
            mallOrderItem.setOrderId((long) i);
            mallOrderItem.setGoodsId((long) i);
            mallOrderItem.setGoodsName(i + "");
            mallOrderItem.setGoodsCoverImg("##");
            mallOrderItem.setSellingPrice(i * 100);
            mallOrderItem.setGoodsCount(i);
            list.add(mallOrderItem);
        }
        logger.info("list:{}", list);
        logger.info("result:{}", String.valueOf(mallOrderItemMapper.insertBatch(list)));
    }

    @Test
    void getMallOrderItemListByOrderIdTest() {
        List<MallOrderItem> mallOrderItems = mallOrderItemMapper.getMallOrderItemListByOrderId(2L);
        logger.info("mallOrderItems:{}", mallOrderItems);
    }

    @Test
    void getMallOrderItemListByOrderIdsTest() {
        List<Long> list = Arrays.asList(0L, 1L, 2L, 3L);
        List<MallOrderItem> mallOrderItems = mallOrderItemMapper.getMallOrderItemListByOrderIds(list);
        logger.info("mallOrderItems:{}", mallOrderItems);
    }
}
