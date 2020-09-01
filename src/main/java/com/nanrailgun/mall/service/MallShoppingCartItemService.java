package com.nanrailgun.mall.service;

import com.nanrailgun.mall.controller.param.MallShoppingCartItemSaveParam;
import com.nanrailgun.mall.controller.param.MallShoppingCartItemUpdateParam;
import com.nanrailgun.mall.controller.vo.MallShoppingCartItemVO;

import java.util.List;

public interface MallShoppingCartItemService {

    /**
     * 保存购物项
     */
    String saveShoppingCartItem(Long userId, MallShoppingCartItemSaveParam param);

    /**
     * 修改购物项
     */
    String updateShoppingCartItem(Long userId, MallShoppingCartItemUpdateParam param);

    /**
     * 删除购物项
     */
    String deleteShoppingCartItem(Long userId, Long cartItemId);

    /**
     * 获取购物项列表
     */
    List<MallShoppingCartItemVO> getAllShoppingCartItem(Long userId);

    List<MallShoppingCartItemVO> getShoppingCartItemByCartItemIds(Long userId, List<Long> cartItemIds);
}
