package com.nanrailgun.mall.service.impl;

import com.nanrailgun.mall.common.*;
import com.nanrailgun.mall.controller.vo.MallOrderDetailVO;
import com.nanrailgun.mall.controller.vo.MallOrderItemVO;
import com.nanrailgun.mall.controller.vo.MallOrderListVO;
import com.nanrailgun.mall.controller.vo.MallShoppingCartItemVO;
import com.nanrailgun.mall.dao.*;
import com.nanrailgun.mall.entity.*;
import com.nanrailgun.mall.service.MallOrderService;
import com.nanrailgun.mall.service.MallShoppingCartItemService;
import com.nanrailgun.mall.utils.MyBeanUtil;
import com.nanrailgun.mall.utils.NumberUtil;
import com.nanrailgun.mall.utils.PageQueryUtil;
import com.nanrailgun.mall.utils.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class MallOrderServiceImpl implements MallOrderService {

    @Resource
    MallOrderMapper mallOrderMapper;

    @Resource
    MallOrderItemMapper mallOrderItemMapper;

    @Resource
    MallOrderAddressMapper mallOrderAddressMapper;

    @Resource
    MallShoppingCartItemMapper mallShoppingCartItemMapper;

    @Resource
    MallGoodsMapper mallGoodsMapper;

    @Autowired
    MallShoppingCartItemService mallShoppingCartItemService;

    @Override
    public MallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        MallOrder mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder == null) {
            MallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!userId.equals(mallOrder.getUserId())) {
            MallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        List<MallOrderItem> mallOrderItems = mallOrderItemMapper.getMallOrderItemListByOrderId(mallOrder.getOrderId());
        if (CollectionUtils.isEmpty(mallOrderItems)) {
            MallException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
        }
        MallOrderDetailVO mallOrderDetailVO = new MallOrderDetailVO();
        mallOrderDetailVO.setMallOrderItemVOS(MyBeanUtil.copyList(mallOrderItems, MallOrderItemVO.class));
        mallOrderDetailVO.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(mallOrder.getOrderStatus()).getName());
        mallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(mallOrder.getPayType()).getName());
        BeanUtils.copyProperties(mallOrder, mallOrderDetailVO);
        return mallOrderDetailVO;
    }

    @Override
    public PageResult<MallOrderListVO> getMyOrders(PageQueryUtil util) {
        int total = mallOrderMapper.getTotalMallOrders(util);
        List<MallOrder> mallOrders = mallOrderMapper.findMallOrderList(util);
        List<MallOrderListVO> mallOrderListVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mallOrders) && total > 0) {
            mallOrderListVOS = MyBeanUtil.copyList(mallOrders, MallOrderListVO.class);
            mallOrderListVOS.forEach(item -> item.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(item.getOrderStatus()).getName()));
            List<Long> orderIds = mallOrderListVOS.stream().map(MallOrderListVO::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<MallOrderItem> mallOrderItems = mallOrderItemMapper.getMallOrderItemListByOrderIds(orderIds);
                Map<Long, List<MallOrderItem>> map = mallOrderItems.stream().collect(groupingBy(MallOrderItem::getOrderId));
                mallOrderListVOS.forEach(item -> item.setMallOrderItemVOS(MyBeanUtil.copyList(map.get(item.getOrderId()), MallOrderItemVO.class)));
            }
        }
        return new PageResult<>(mallOrderListVOS, total, util.getLimit(), util.getPage());
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        MallOrder order = mallOrderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            if (!userId.equals(order.getUserId())) {
                return ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult();
            }
            if (mallOrderMapper.closeOrder(order.getOrderId(), MallOrderStatusEnum.ORDER_CLOSED_BY_USER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        MallOrder order = mallOrderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            if (!userId.equals(order.getUserId())) {
                return ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult();
            }
            order.setOrderStatus((byte) MallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            order.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKey(order) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public String pay(String orderNo, int payType) {
        MallOrder mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null) {
            if (mallOrder.getOrderStatus().intValue() != MallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                MallException.fail("非待支付状态下的订单无法支付");
            }
            mallOrder.setOrderStatus((byte) MallOrderStatusEnum.ORDER_PAID.getOrderStatus());
            mallOrder.setPayType((byte) payType);
            mallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            mallOrder.setPayTime(new Date());
            mallOrder.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKey(mallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(MallUser user, List<Long> cartItemIds, MallUserAddress address) {
        List<MallShoppingCartItemVO> shoppingCartItems = mallShoppingCartItemService.getShoppingCartItemByCartItemIds(user.getUserId(), cartItemIds);
        Map<Long, MallShoppingCartItemVO> mallShoppingCartItemVOMap = shoppingCartItems.stream().collect(Collectors.toMap(MallShoppingCartItemVO::getGoodsId, Function.identity()));
        List<Long> goodsIdList = shoppingCartItems.stream().map(MallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<Long> mallShoppingCartItemIds = shoppingCartItems.stream().map(MallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<MallGoods> goodsList = mallGoodsMapper.selectByPrimaryKeys(goodsIdList);
        if (shoppingCartItems.size() != cartItemIds.size() || goodsList.size() != cartItemIds.size()) {
            MallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        goodsList.forEach(item -> {
            if (item.getGoodsSellStatus() != Constants.SELL_STATUS_UP) {
                MallException.fail(item.getGoodsName() + "已下架，无法生成订单！");
            }
            if (item.getStockNum() < mallShoppingCartItemVOMap.get(item.getGoodsId()).getGoodsCount()) {
                MallException.fail(item.getGoodsName() + "库存不足，无法生成订单！");
            }
        });
        //删除购物项
        if (mallShoppingCartItemMapper.deleteBatch(mallShoppingCartItemIds) < 1) {
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        //减库存
        List<StockNum> stockNums = MyBeanUtil.copyList(shoppingCartItems, StockNum.class);
        int result = mallGoodsMapper.updateStockNum(stockNums);
        if (result < 1) {
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        String orderNo = NumberUtil.genOrderNo();
        int totalPrice = 0;
        MallOrder order = new MallOrder();
        order.setOrderNo(orderNo);
        order.setUserId(user.getUserId());
        //计算总价
        for (MallShoppingCartItemVO item : shoppingCartItems) {
            totalPrice = totalPrice + item.getGoodsCount() * item.getSellingPrice();
        }
        if (totalPrice < 1) {
            MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
        }
        order.setTotalPrice(totalPrice);
        order.setExtraInfo("");
        if (mallOrderMapper.insert(order) < 1) {
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        MallOrderAddress mallOrderAddress = new MallOrderAddress();
        order = mallOrderMapper.selectByOrderNo(orderNo);
        mallOrderAddress.setOrderId(order.getOrderId());
        BeanUtils.copyProperties(address, mallOrderAddress);
        if (mallOrderAddressMapper.insert(mallOrderAddress) < 1) {
            MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
        }
        List<MallOrderItem> mallOrderItems = MyBeanUtil.copyList(shoppingCartItems, MallOrderItem.class);
        MallOrder temp = order;
        mallOrderItems.forEach(item -> item.setOrderId(temp.getOrderId()));
        if (mallOrderItemMapper.insertBatch(mallOrderItems) < 1) {
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        return orderNo;
    }
}
