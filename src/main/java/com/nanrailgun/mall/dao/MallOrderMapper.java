package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallOrder;
import com.nanrailgun.mall.utils.PageQueryUtil;

import java.util.List;

public interface MallOrderMapper {

    int insert(MallOrder order);

    int updateByPrimaryKey(MallOrder order);

    int closeOrder(Long orderId, int status);

    MallOrder selectByOrderNo(String orderNo);

    List<MallOrder> findMallOrderList(PageQueryUtil util);

    int getTotalMallOrders(PageQueryUtil util);
}
