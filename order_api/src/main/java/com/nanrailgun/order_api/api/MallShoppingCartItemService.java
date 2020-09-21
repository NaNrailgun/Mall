package com.nanrailgun.order_api.api;

import com.nanrailgun.order_api.api.dto.MallShoppingCartItemDTO;
import com.nanrailgun.order_api.api.dto.MallShoppingCartItemSaveParamDTO;
import com.nanrailgun.order_api.api.dto.MallShoppingCartItemUpdateParamDTO;

import java.util.List;

public interface MallShoppingCartItemService {

    /**
     * 保存购物项
     */
    String saveShoppingCartItem(Long userId, MallShoppingCartItemSaveParamDTO param);

    /**
     * 修改购物项
     */
    String updateShoppingCartItem(Long userId, MallShoppingCartItemUpdateParamDTO param);

    /**
     * 删除购物项
     */
    String deleteShoppingCartItem(Long userId, Long cartItemId);

    /**
     * 获取购物项列表
     */
    List<MallShoppingCartItemDTO> getAllShoppingCartItem(Long userId);

    List<MallShoppingCartItemDTO> getShoppingCartItemByCartItemIds(Long userId, List<Long> cartItemIds);
}
