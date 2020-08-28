package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallGoodsCategory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class MallGoodsCategoryMapperTest {

    @Resource
    MallGoodsCategoryMapper mallGoodsCategoryMapper;

    private static final Logger logger = LoggerFactory.getLogger(MallGoodsCategoryMapperTest.class);

    @Test
    void selectByLevelAndParentIdsAndNumber() {
        List<Long> list = Collections.singletonList(0L);
        int level = 1;
        List<MallGoodsCategory> categoryList = mallGoodsCategoryMapper.selectByLevelAndParentIdsAndNumber(list, level, 0);
        logger.info("list:{}", list);
        logger.info("level:{}", level);
        logger.info("categoryList:{}", categoryList);
    }

}
