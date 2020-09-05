package com.nanrailgun.mall.service.impl;

import com.nanrailgun.mall.common.*;
import com.nanrailgun.mall.controller.vo.MallOrderDetailVO;
import com.nanrailgun.mall.controller.vo.MallOrderItemVO;
import com.nanrailgun.mall.controller.vo.MallOrderListVO;
import com.nanrailgun.mall.controller.vo.MallShoppingCartItemVO;
import com.nanrailgun.mall.dao.*;
import com.nanrailgun.mall.entity.MallOrder;
import com.nanrailgun.mall.entity.MallOrderItem;
import com.nanrailgun.mall.entity.MallUserAddress;
import com.nanrailgun.mall.service.MallOrderService;
import com.nanrailgun.mall.utils.MyBeanUtil;
import com.nanrailgun.mall.utils.PageQueryUtil;
import com.nanrailgun.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        mallOrderDetailVO.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(mallOrderDetailVO.getOrderStatus()).getName());
        mallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(mallOrderDetailVO.getPayType()).getName());
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
    public String saveOrder(Long userId, MallUserAddress address, List<MallShoppingCartItemVO> items) {
        return null;
    }
}
