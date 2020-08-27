package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallGoods;
import com.nanrailgun.mall.entity.StockNum;
import com.nanrailgun.mall.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallGoodsMapper {

    MallGoods selectByPrimaryKey(Long goodsId);

    List<MallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<MallGoods> selectBySearch(PageQueryUtil pageUtil);

    int getTotalMallGoodsBySearch(PageQueryUtil pageUtil);

    int updateStockNum(@Param("stockNums") List<StockNum> stockNum);
}
