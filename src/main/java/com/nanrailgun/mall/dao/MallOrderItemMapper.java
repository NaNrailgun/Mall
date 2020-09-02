package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallOrderItem;

import java.util.List;

public interface MallOrderItemMapper {
    List<MallOrderItem> getMallOrderItemListByOrderId(Long orderId);

    List<MallOrderItem> getMallOrderItemListByOrderIds(List<Long> orderIds);

    int insertBatch(List<MallOrderItem> mallOrderItems);
}
