package com.nanrailgun.mall.controller;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.config.annotation.MallToken;
import com.nanrailgun.mall.controller.param.MallOrderSaveParam;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.entity.MallUserAddress;
import com.nanrailgun.mall.service.MallOrderService;
import com.nanrailgun.mall.service.MallUserAddressService;
import com.nanrailgun.mall.utils.PageQueryUtil;
import com.nanrailgun.mall.utils.Result;
import com.nanrailgun.mall.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MallOrderController {

    @Autowired
    MallOrderService mallOrderService;

    @Autowired
    MallUserAddressService mallUserAddressService;

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

    @GetMapping("/paySuccess")
    public Result mockPaySuccess(@RequestParam("orderNo") String orderNo, @RequestParam("payType") int payType, @MallToken MallUser user) {
        String result = mallOrderService.pay(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @PostMapping("/saveOrder")
    public Result saveOrder(@RequestBody MallOrderSaveParam param, @MallToken MallUser user) {
        if (param == null || param.getAddressId() == null || param.getCartItemIds() == null || param.getCartItemIds().length < 1) {
            return ResultGenerator.genFailResult(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        MallUserAddress address = mallUserAddressService.getAddressByAddressId(param.getAddressId());
        if (!address.getUserId().equals(user.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        List<Long> cartItemIds = Arrays.asList(param.getCartItemIds());
        String orderNo = mallOrderService.saveOrder(user, cartItemIds, address);
        return ResultGenerator.genSuccessResult((Object) orderNo);
    }
}
