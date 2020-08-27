package com.nanrailgun.mall.service;

import com.nanrailgun.mall.entity.MallGoods;
import com.nanrailgun.mall.utils.PageQueryUtil;
import com.nanrailgun.mall.utils.PageResult;

public interface MallGoodsService {

    /**
     * 获取商品详情
     */
    MallGoods getGoodsDetailByGoodsId(Long goodsId);

    /**
     * 商品搜索
     */
    PageResult getGoodsListBySearch(PageQueryUtil pageUtil);
}
