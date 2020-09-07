package com.nanrailgun.mall.controller;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.config.annotation.MallToken;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.service.MallOrderService;
import com.nanrailgun.mall.utils.PageQueryUtil;
import com.nanrailgun.mall.utils.Result;
import com.nanrailgun.mall.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MallOrderController {

    @Autowired
    MallOrderService mallOrderService;

    @GetMapping("/order")
    public Result getOrderList(@RequestParam(required = false) Integer pageNumber,
                               @RequestParam(required = false) Integer status,
                               @MallToken MallUser loginMallUser) {
        Map params = new HashMap();
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUser.getUserId());
        params.put("orderStatus", status);
        params.put("page", pageNumber);
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        PageQueryUtil util = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallOrderService.getMyOrders(util));
    }

    @GetMapping("/order/{orderNo}")
    public Result getOrderDetail(@PathVariable("orderNo") String orderNo, @MallToken MallUser user) {
        return ResultGenerator.genSuccessResult(mallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId()));
    }

    @PutMapping("/order/{orderNo}/cancel")
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, @MallToken MallUser user) {
        String result = mallOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @PutMapping("/order/{orderNo}/finish")
    public Result finishOrder(@PathVariable("orderNo") String orderNo, @MallToken MallUser user) {
        String result = mallOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }
}
