package com.nanrailgun.mall.service;

import com.nanrailgun.mall.controller.vo.MallIndexCategoryVO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MallGoodsCategoryServiceTest {

    @Autowired
    MallGoodsCategoryService mallGoodsCategoryService;

    private static final Logger logger = LoggerFactory.getLogger(MallGoodsCategoryServiceTest.class);

    @Test
    void getAllGoodsCategory() {
        List<MallIndexCategoryVO> list = mallGoodsCategoryService.getAllGoodsCategory();
        logger.info("list:{}", list);
    }

}
