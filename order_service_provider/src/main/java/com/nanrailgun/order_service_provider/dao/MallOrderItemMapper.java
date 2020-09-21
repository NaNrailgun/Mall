package com.nanrailgun.order_service_provider.dao;

import com.nanrailgun.order_api.entity.MallOrderItem;

import java.util.List;

public interface MallOrderItemMapper {
    List<MallOrderItem> getMallOrderItemListByOrderId(Long orderId);

    List<MallOrderItem> getMallOrderItemListByOrderIds(List<Long> orderIds);

    int insertBatch(List<MallOrderItem> mallOrderItems);
}
