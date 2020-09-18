package com.nanrailgun.goods_api.api;

import com.nanrailgun.goods_api.api.dto.MallIndexCategoryDTO;

import java.util.List;

public interface MallGoodsCategoryService {
    /**
     * 查询所有分类
     */
    List<MallIndexCategoryDTO> getAllGoodsCategory();
}
