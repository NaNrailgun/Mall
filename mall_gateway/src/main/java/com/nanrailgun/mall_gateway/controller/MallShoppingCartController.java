package com.nanrailgun.mall_gateway.controller;

import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.config.config.annotation.MallToken;
import com.nanrailgun.config.utils.Result;
import com.nanrailgun.config.utils.ResultGenerator;
import com.nanrailgun.mall_gateway.controller.param.MallShoppingCartItemSaveParam;
import com.nanrailgun.mall_gateway.controller.param.MallShoppingCartItemUpdateParam;
import com.nanrailgun.order_api.api.MallShoppingCartItemService;
import com.nanrailgun.order_api.api.dto.MallShoppingCartItemDTO;
import com.nanrailgun.order_api.api.dto.MallShoppingCartItemSaveParamDTO;
import com.nanrailgun.order_api.api.dto.MallShoppingCartItemUpdateParamDTO;
import com.nanrailgun.user_api.entity.MallUser;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class MallShoppingCartController {

    @Reference
    private MallShoppingCartItemService mallShoppingCartItemService;

    @GetMapping("/shop-cart")
    public Result getAllCartItem(@MallToken MallUser user) {
        return ResultGenerator.genSuccessResult(mallShoppingCartItemService.getAllShoppingCartItem(user.getUserId()));
    }

    @PostMapping("/shop-cart")
    public Result saveCartItem(@MallToken MallUser user, @RequestBody MallShoppingCartItemSaveParam param) {
        MallShoppingCartItemSaveParamDTO dto = new MallShoppingCartItemSaveParamDTO();
        BeanUtils.copyProperties(param, dto);
        String result = mallShoppingCartItemService.saveShoppingCartItem(user.getUserId(), dto);
        if (result.equals(ServiceResultEnum.SUCCESS.getResult())) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @PutMapping("shop-cart")
    public Result updateCartItem(@MallToken MallUser user, @RequestBody MallShoppingCartItemUpdateParam param) {
        MallShoppingCartItemUpdateParamDTO dto = new MallShoppingCartItemUpdateParamDTO();
        BeanUtils.copyProperties(param, dto);
        String result = mallShoppingCartItemService.updateShoppingCartItem(user.getUserId(), dto);
        if (result.equals(ServiceResultEnum.SUCCESS.getResult())) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @DeleteMapping("/shop-cart/{newBeeMallShoppingCartItemId}")
    public Result deleteCartItem(@MallToken MallUser user, @PathVariable("newBeeMallShoppingCartItemId") Long cartItemId) {
        String result = mallShoppingCartItemService.deleteShoppingCartItem(user.getUserId(), cartItemId);
        if (result.equals(ServiceResultEnum.SUCCESS.getResult())) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    /**
     * 获取购物项明细（确认订单页面使用）
     */
    @GetMapping("/shop-cart/settle")
    public Result toSettle(@MallToken MallUser user, Long[] cartItemIds) {
        List<MallShoppingCartItemDTO> list = mallShoppingCartItemService.getShoppingCartItemByCartItemIds(user.getUserId(), Arrays.asList(cartItemIds));
        if (cartItemIds.length < 1 || CollectionUtils.isEmpty(list) || list.size() != cartItemIds.length) {
            return ResultGenerator.genFailResult("参数异常");
        }
        int totalPrice = 0;
        for (MallShoppingCartItemDTO dto : list) {
            totalPrice = totalPrice + dto.getSellingPrice() * dto.getGoodsCount();
        }
        if (totalPrice < 1) return ResultGenerator.genFailResult("价格异常");
        return ResultGenerator.genSuccessResult(list);
    }

}
