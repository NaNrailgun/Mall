package com.nanrailgun.mall.service.impl;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.controller.param.MallShoppingCartItemSaveParam;
import com.nanrailgun.mall.controller.param.MallShoppingCartItemUpdateParam;
import com.nanrailgun.mall.controller.vo.MallShoppingCartItemVO;
import com.nanrailgun.mall.dao.MallGoodsMapper;
import com.nanrailgun.mall.dao.MallShoppingCartItemMapper;
import com.nanrailgun.mall.entity.MallGoods;
import com.nanrailgun.mall.entity.MallShoppingCartItem;
import com.nanrailgun.mall.service.MallShoppingCartItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Service
public class MallShoppingCartItemServiceImpl implements MallShoppingCartItemService {

    @Resource
    MallShoppingCartItemMapper mallShoppingCartItemMapper;

    @Resource
    MallGoodsMapper mallGoodsMapper;

    @Override
    public String saveShoppingCartItem(Long userId, MallShoppingCartItemSaveParam param) {
        MallShoppingCartItem cartItem = mallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, param.getGoodsId());
        if (cartItem != null) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult();
        }
        MallGoods goods = mallGoodsMapper.selectByPrimaryKey(param.getGoodsId());
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
    public String updateShoppingCartItem(Long userId, MallShoppingCartItemUpdateParam param) {
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
    public List<MallShoppingCartItemVO> getAllShoppingCartItem(Long userId) {
        return getMallShoppingCartItemVOList(mallShoppingCartItemMapper.selectByUserId(userId));
    }

    @Override
    public List<MallShoppingCartItemVO> getShoppingCartItemByCartItemIds(Long userId, List<Long> cartItemIds) {
        List<MallShoppingCartItem> list = mallShoppingCartItemMapper.selectByPrimaryKeys(cartItemIds);
        list.removeIf(item -> !item.getUserId().equals(userId));
        return getMallShoppingCartItemVOList(list);
    }

    private List<MallShoppingCartItemVO> getMallShoppingCartItemVOList(List<MallShoppingCartItem> mallShoppingCartItems) {
        List<MallShoppingCartItemVO> list = new ArrayList<>();
        List<Long> goodsIds = mallShoppingCartItems.stream().map(MallShoppingCartItem::getGoodsId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsIds)) {
            List<MallGoods> mallGoodsList = mallGoodsMapper.selectByPrimaryKeys(goodsIds);
            Map<Long, MallGoods> mallGoodsMap;
            if (!CollectionUtils.isEmpty(mallGoodsList)) {
                mallGoodsMap = mallGoodsList.stream().collect(Collectors.toMap(MallGoods::getGoodsId, identity()));
                mallShoppingCartItems.forEach((item) -> {
                    if (mallGoodsMap.containsKey(item.getGoodsId())) {
                        MallShoppingCartItemVO vo = new MallShoppingCartItemVO();
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
