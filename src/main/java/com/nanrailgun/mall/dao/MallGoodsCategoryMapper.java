package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallGoodsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallGoodsCategoryMapper {

    List<MallGoodsCategory> selectByLevelAndParentIdsAndNumber(@Param("parentIds") List<Long> parentIds,
                                                               @Param("categoryLevel") int categoryLevel,
                                                               @Param("number") int number);
}
