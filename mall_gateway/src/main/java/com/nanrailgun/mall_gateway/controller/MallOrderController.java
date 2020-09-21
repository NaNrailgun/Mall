package com.nanrailgun.mall_gateway.controller;

import com.nanrailgun.config.common.Constants;
import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.config.config.annotation.MallToken;
import com.nanrailgun.config.utils.PageQueryUtil;
import com.nanrailgun.config.utils.Result;
import com.nanrailgun.config.utils.ResultGenerator;
import com.nanrailgun.mall_gateway.controller.param.MallOrderSaveParam;
import com.nanrailgun.order_api.api.MallOrderService;
import com.nanrailgun.user_api.api.MallUserAddressService;
import com.nanrailgun.user_api.entity.MallUser;
import com.nanrailgun.user_api.entity.MallUserAddress;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MallOrderController {

    @Reference
    private MallOrderService mallOrderService;

    @Reference
    private MallUserAddressService mallUserAddressService;

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
        return ResultGenerator.genSuccessResult(mallOrderService.getMyOrders(util, util.getPage(), util.getLimit()));
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
