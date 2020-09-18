package com.nanrailgun.goods_service_provider.dao;

import com.nanrailgun.goods_api.entity.MallGoodsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallGoodsCategoryMapper {

    List<MallGoodsCategory> selectByLevelAndParentIdsAndNumber(@Param("parentIds") List<Long> parentIds,
                                                               @Param("categoryLevel") int categoryLevel,
                                                               @Param("number") int number);
}
