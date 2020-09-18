package com.nanrailgun.goods_service_provider.dao;

import com.nanrailgun.config.utils.PageQueryUtil;
import com.nanrailgun.goods_api.entity.MallGoods;
import com.nanrailgun.goods_api.entity.StockNum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallGoodsMapper {

    MallGoods selectByPrimaryKey(Long goodsId);

    List<MallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<MallGoods> selectBySearch(PageQueryUtil pageUtil);

    int getTotalMallGoodsBySearch(PageQueryUtil pageUtil);

    int updateStockNum(@Param("stockNums") List<StockNum> stockNum);
}
