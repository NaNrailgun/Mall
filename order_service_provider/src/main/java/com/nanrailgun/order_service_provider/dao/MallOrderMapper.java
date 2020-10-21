package com.nanrailgun.order_service_provider.dao;

import com.nanrailgun.config.utils.PageQueryUtil;
import com.nanrailgun.order_api.entity.MallOrder;

import java.util.List;

public interface MallOrderMapper {

    int insert(MallOrder order);

    int updateByPrimaryKey(MallOrder order);

    int closeOrder(Long orderId, int status);

    MallOrder selectByOrderNo(String orderNo);

    MallOrder selectByOrderNoForUpdate(String orderNo);

    List<MallOrder> findMallOrderList(PageQueryUtil util);

    int getTotalMallOrders(PageQueryUtil util);
}
