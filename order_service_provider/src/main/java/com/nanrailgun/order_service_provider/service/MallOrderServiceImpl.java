package com.nanrailgun.order_service_provider.service;


import com.nanrailgun.config.common.*;
import com.nanrailgun.config.utils.MyBeanUtil;
import com.nanrailgun.config.utils.PageQueryUtil;
import com.nanrailgun.config.utils.PageResult;
import com.nanrailgun.goods_api.api.MallGoodsService;
import com.nanrailgun.goods_api.entity.MallGoods;
import com.nanrailgun.goods_api.entity.StockNum;
import com.nanrailgun.order_api.api.MallOrderService;
import com.nanrailgun.order_api.api.MallShoppingCartItemService;
import com.nanrailgun.order_api.api.dto.MallOrderDetailDTO;
import com.nanrailgun.order_api.api.dto.MallOrderItemDTO;
import com.nanrailgun.order_api.api.dto.MallOrderListDTO;
import com.nanrailgun.order_api.api.dto.MallShoppingCartItemDTO;
import com.nanrailgun.order_api.entity.MallOrder;
import com.nanrailgun.order_api.entity.MallOrderAddress;
import com.nanrailgun.order_api.entity.MallOrderItem;
import com.nanrailgun.order_service_provider.dao.MallOrderAddressMapper;
import com.nanrailgun.order_service_provider.dao.MallOrderItemMapper;
import com.nanrailgun.order_service_provider.dao.MallOrderMapper;
import com.nanrailgun.order_service_provider.dao.MallShoppingCartItemMapper;
import com.nanrailgun.springbootstartersnowflake.beans.IdWorker;
import com.nanrailgun.user_api.entity.MallUser;
import com.nanrailgun.user_api.entity.MallUserAddress;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Reference
    private MallGoodsService mallGoodsService;

    @Autowired
    MallShoppingCartItemService mallShoppingCartItemService;

    @Autowired
    IdWorker idWorker;

    @Override
    public MallOrderDetailDTO getOrderDetailByOrderNo(String orderNo, Long userId) {
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
        MallOrderDetailDTO mallOrderDetailDTO = new MallOrderDetailDTO();
        mallOrderDetailDTO.setMallOrderItemVOS(MyBeanUtil.copyList(mallOrderItems, MallOrderItemDTO.class));
        mallOrderDetailDTO.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(mallOrder.getOrderStatus()).getName());
        mallOrderDetailDTO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(mallOrder.getPayType()).getName());
        BeanUtils.copyProperties(mallOrder, mallOrderDetailDTO);
        return mallOrderDetailDTO;
    }

    @Override
    public PageResult<MallOrderListDTO> getMyOrders(PageQueryUtil util, int page, int limit) {
        util.setPage(page);
        util.setLimit(limit);
        int total = mallOrderMapper.getTotalMallOrders(util);
        List<MallOrder> mallOrders = mallOrderMapper.findMallOrderList(util);
        List<MallOrderListDTO> mallOrderListVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mallOrders) && total > 0) {
            mallOrderListVOS = MyBeanUtil.copyList(mallOrders, MallOrderListDTO.class);
            mallOrderListVOS.forEach(item -> item.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(item.getOrderStatus()).getName()));
            List<Long> orderIds = mallOrderListVOS.stream().map(MallOrderListDTO::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<MallOrderItem> mallOrderItems = mallOrderItemMapper.getMallOrderItemListByOrderIds(orderIds);
                Map<Long, List<MallOrderItem>> map = mallOrderItems.stream().collect(groupingBy(MallOrderItem::getOrderId));
                mallOrderListVOS.forEach(item -> item.setMallOrderItemVOS(MyBeanUtil.copyList(map.get(item.getOrderId()), MallOrderItemDTO.class)));
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

    /**
     * 锁订单->检测订单状态|支付状态->修改支付状态
     */
    @Override
    @Transactional
    public String pay(String orderNo, int payType) {
        //加锁
        MallOrder mallOrder = mallOrderMapper.selectByOrderNoForUpdate(orderNo);
        if (mallOrder != null) {
            //检测订单状态 是否为待支付
            //检测支付状态 是否为待支付
            if (mallOrder.getOrderStatus().intValue() != MallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()
                    && mallOrder.getPayStatus() != PayStatusEnum.DEFAULT.getPayStatus()) {
                MallException.fail("非待支付状态下的订单无法支付");
            }
            //设置支付方式
            mallOrder.setPayType((byte) payType);
            //支付状态修改为 支付中
            mallOrder.setPayStatus((byte) PayStatusEnum.PAY_ING.getPayStatus());
            mallOrder.setPayTime(new Date());
            mallOrder.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKey(mallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
        }
        MallException.fail(ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult());
        return "";
    }

    /**
     * 加锁->判断订单状态|支付状态->修改支付状态&订单状态
     */
    @Override
    @Transactional
    public String paySuccess(String orderNo) {
        //加锁
        MallOrder mallOrder = mallOrderMapper.selectByOrderNoForUpdate(orderNo);
        if (mallOrder != null) {
            //判断订单状态|支付状态
            if (mallOrder.getOrderStatus().intValue() != MallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                MallException.fail("订单状态异常");
            }
            if (mallOrder.getPayStatus() != PayStatusEnum.PAY_ING.getPayStatus()) {
                MallException.fail("支付异常");
            }
            //修改订单状态 为已支付
            mallOrder.setOrderStatus((byte) MallOrderStatusEnum.ORDER_PAID.getOrderStatus());
            //修改支付状态 为支付成功
            mallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            mallOrder.setPayTime(new Date());
            mallOrder.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKey(mallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
        }
        MallException.fail(ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult());
        return "";
    }

    @Override
    @GlobalTransactional
    public String saveOrder(MallUser user, List<Long> cartItemIds, MallUserAddress address) {
        List<MallShoppingCartItemDTO> shoppingCartItems = mallShoppingCartItemService.getShoppingCartItemByCartItemIds(user.getUserId(), cartItemIds);
        Map<Long, MallShoppingCartItemDTO> mallShoppingCartItemVOMap = shoppingCartItems.stream().collect(Collectors.toMap(MallShoppingCartItemDTO::getGoodsId, Function.identity()));
        List<Long> goodsIdList = shoppingCartItems.stream().map(MallShoppingCartItemDTO::getGoodsId).collect(Collectors.toList());
        List<Long> mallShoppingCartItemIds = shoppingCartItems.stream().map(MallShoppingCartItemDTO::getCartItemId).collect(Collectors.toList());
        List<MallGoods> goodsList = mallGoodsService.selectByPrimaryKeys(goodsIdList);
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
        int result = mallGoodsService.updateStockNum(stockNums);
        if (result < 1) {
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        String orderNo = String.valueOf(idWorker.nextId());
        int totalPrice = 0;
        MallOrder order = new MallOrder();
        order.setOrderNo(orderNo);
        order.setUserId(user.getUserId());
        //计算总价
        for (MallShoppingCartItemDTO item : shoppingCartItems) {
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

        //测试分布式事务
        //throw new MallException("test");

        return orderNo;
    }
}
