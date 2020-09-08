package com.nanrailgun.mall.service;

import com.nanrailgun.mall.controller.vo.MallOrderDetailVO;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.entity.MallUserAddress;
import com.nanrailgun.mall.utils.PageQueryUtil;
import com.nanrailgun.mall.utils.PageResult;

import java.util.List;

public interface MallOrderService {

    MallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    PageResult getMyOrders(PageQueryUtil util);

    String cancelOrder(String orderNo, Long userId);

    String finishOrder(String orderNo, Long userId);

    String saveOrder(MallUser user, List<Long> cartItemIds, MallUserAddress address);

    String pay(String orderNo, int payType);
}
