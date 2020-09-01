package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallShoppingCartItem;

import java.util.List;

public interface MallShoppingCartItemMapper {

    int insert(MallShoppingCartItem shoppingCartItem);

    MallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    MallShoppingCartItem selectByUserIdAndGoodsId(Long userId, Long goodsId);

    List<MallShoppingCartItem> selectByUserId(Long userId);

    List<MallShoppingCartItem> selectByPrimaryKeys(List<Long> cartItemIds);

    int selectCountByUserId(Long userId);

    int updateByPrimaryKey(MallShoppingCartItem item);

    int deleteByPrimary(Long cartItemId);

    int deleteBatch(List<Long> ids);
}
