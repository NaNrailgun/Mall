package com.nanrailgun.order_api.api;

import com.nanrailgun.config.utils.PageQueryUtil;
import com.nanrailgun.config.utils.PageResult;
import com.nanrailgun.order_api.api.dto.MallOrderDetailDTO;
import com.nanrailgun.user_api.entity.MallUser;
import com.nanrailgun.user_api.entity.MallUserAddress;

import java.util.List;

public interface MallOrderService {

    MallOrderDetailDTO getOrderDetailByOrderNo(String orderNo, Long userId);

    PageResult getMyOrders(PageQueryUtil util,int page,int limit);

    String cancelOrder(String orderNo, Long userId);

    String finishOrder(String orderNo, Long userId);

    String saveOrder(MallUser user, List<Long> cartItemIds, MallUserAddress address);

    String pay(String orderNo, int payType);

    String paySuccess(String orderNo);
}
