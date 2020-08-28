package com.nanrailgun.mall.service;

import com.nanrailgun.mall.controller.vo.MallIndexCategoryVO;

import java.util.List;

public interface MallGoodsCategoryService {
    /**
     * 查询所有分类
     */
    List<MallIndexCategoryVO> getAllGoodsCategory();
}
