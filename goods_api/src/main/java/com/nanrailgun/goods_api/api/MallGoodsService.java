package com.nanrailgun.goods_api.api;

import com.nanrailgun.config.utils.PageQueryUtil;
import com.nanrailgun.config.utils.PageResult;
import com.nanrailgun.goods_api.entity.MallGoods;

public interface MallGoodsService {
    /**
     * 获取商品详情
     */
    MallGoods getGoodsDetailByGoodsId(Long goodsId);

    /**
     * 商品搜索
     */
    PageResult getGoodsListBySearch(PageQueryUtil pageUtil, int page, int limit);
}
