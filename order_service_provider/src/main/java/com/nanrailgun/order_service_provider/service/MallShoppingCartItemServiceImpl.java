package com.nanrailgun.order_service_provider.service;


import com.nanrailgun.config.common.Constants;
import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.goods_api.api.MallGoodsService;
import com.nanrailgun.goods_api.entity.MallGoods;
import com.nanrailgun.order_api.api.MallShoppingCartItemService;
import com.nanrailgun.order_api.api.dto.MallShoppingCartItemDTO;
import com.nanrailgun.order_api.api.dto.MallShoppingCartItemSaveParamDTO;
import com.nanrailgun.order_api.api.dto.MallShoppingCartItemUpdateParamDTO;
import com.nanrailgun.order_api.entity.MallShoppingCartItem;
import com.nanrailgun.order_service_provider.dao.MallShoppingCartItemMapper;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Service
@org.springframework.stereotype.Service
public class MallShoppingCartItemServiceImpl implements MallShoppingCartItemService {

    @Resource
    MallShoppingCartItemMapper mallShoppingCartItemMapper;

    @Reference
    private MallGoodsService mallGoodsService;

    @Override
    public String saveShoppingCartItem(Long userId, MallShoppingCartItemSaveParamDTO param) {
        MallShoppingCartItem cartItem = mallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, param.getGoodsId());
        if (cartItem != null) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult();
        }
        MallGoods goods = mallGoodsService.selectByPrimaryKey(param.getGoodsId());
        if (goods == null) return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        int totalCount = mallShoppingCartItemMapper.selectCountByUserId(userId);
        if (param.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }
        if (param.getGoodsCount() > Constants.SHOPPING_CART_PAGE_LIMIT) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        if (totalCount > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        MallShoppingCartItem item = new MallShoppingCartItem();
        BeanUtils.copyProperties(param, item);
        item.setUserId(userId);
        if (mallShoppingCartItemMapper.insert(item) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateShoppingCartItem(Long userId, MallShoppingCartItemUpdateParamDTO param) {
        MallShoppingCartItem item = mallShoppingCartItemMapper.selectByPrimaryKey(param.getCartItemId());
        if (item == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!item.getUserId().equals(userId)) {
            return ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult();
        }
        if (param.getGoodsCount() > Constants.SHOPPING_CART_PAGE_LIMIT) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        item.setGoodsCount(param.getGoodsCount());
        item.setUpdateTime(new Date());
        if (mallShoppingCartItemMapper.updateByPrimaryKey(item) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String deleteShoppingCartItem(Long userId, Long cartItemId) {
        MallShoppingCartItem item = mallShoppingCartItemMapper.selectByPrimaryKey(cartItemId);
        if (item == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!item.getUserId().equals(userId)) {
            return ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult();
        }
        if (mallShoppingCartItemMapper.deleteByPrimary(cartItemId) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public List<MallShoppingCartItemDTO> getAllShoppingCartItem(Long userId) {
        return getMallShoppingCartItemVOList(mallShoppingCartItemMapper.selectByUserId(userId));
    }

    @Override
    public List<MallShoppingCartItemDTO> getShoppingCartItemByCartItemIds(Long userId, List<Long> cartItemIds) {
        List<MallShoppingCartItem> list = mallShoppingCartItemMapper.selectByPrimaryKeys(cartItemIds);
        list.removeIf(item -> !item.getUserId().equals(userId));
        return getMallShoppingCartItemVOList(list);
    }

    private List<MallShoppingCartItemDTO> getMallShoppingCartItemVOList(List<MallShoppingCartItem> mallShoppingCartItems) {
        List<MallShoppingCartItemDTO> list = new ArrayList<>();
        List<Long> goodsIds = mallShoppingCartItems.stream().map(MallShoppingCartItem::getGoodsId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsIds)) {
            List<MallGoods> mallGoodsList = mallGoodsService.selectByPrimaryKeys(goodsIds);
            Map<Long, MallGoods> mallGoodsMap;
            if (!CollectionUtils.isEmpty(mallGoodsList)) {
                mallGoodsMap = mallGoodsList.stream().collect(Collectors.toMap(MallGoods::getGoodsId, identity()));
                mallShoppingCartItems.forEach((item) -> {
                    if (mallGoodsMap.containsKey(item.getGoodsId())) {
                        MallShoppingCartItemDTO vo = new MallShoppingCartItemDTO();
                        BeanUtils.copyProperties(item, vo);
                        BeanUtils.copyProperties(mallGoodsMap.get(vo.getGoodsId()), vo);
                        if (vo.getGoodsName().length() > 28) {
                            vo.setGoodsName(vo.getGoodsName().substring(0, 28) + "...");
                        }
                        list.add(vo);
                    }
                });
            }
        }
        return list;
    }
}
