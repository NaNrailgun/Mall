package com.nanrailgun.mall.service;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.utils.PageQueryUtil;
import com.nanrailgun.mall.utils.PageResult;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MallGoodsServiceTest {

    @Autowired
    MallGoodsService mallGoodsService;

    private static final Logger logger = LoggerFactory.getLogger(MallGoodsServiceTest.class);

    @Test
    void getGoodsListBySearchTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("goodsCategoryId", 0);
        params.put("page", 3);
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        params.put("keyword", "1");
        params.put("orderBy", "new");
        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
        PageQueryUtil util = new PageQueryUtil(params);
        logger.info("params:{}", params);
        logger.info("pageUtils:{}", util);
        PageResult pageResult = mallGoodsService.getGoodsListBySearch(util);
        logger.info("pageResult:{}", pageResult);
    }

}
