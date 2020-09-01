package com.nanrailgun.mall.controller;

import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.config.annotation.MallToken;
import com.nanrailgun.mall.controller.param.MallShoppingCartItemSaveParam;
import com.nanrailgun.mall.controller.param.MallShoppingCartItemUpdateParam;
import com.nanrailgun.mall.controller.vo.MallShoppingCartItemVO;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.service.MallShoppingCartItemService;
import com.nanrailgun.mall.utils.Result;
import com.nanrailgun.mall.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class MallShoppingCartController {

    @Autowired
    MallShoppingCartItemService mallShoppingCartItemService;

    @GetMapping("/shop-cart")
    public Result getAllCartItem(@MallToken MallUser user) {
        return ResultGenerator.genSuccessResult(mallShoppingCartItemService.getAllShoppingCartItem(user.getUserId()));
    }

    @PostMapping("/shop-cart")
    public Result saveCartItem(@MallToken MallUser user, @RequestBody MallShoppingCartItemSaveParam param) {
        String result = mallShoppingCartItemService.saveShoppingCartItem(user.getUserId(), param);
        if (result.equals(ServiceResultEnum.SUCCESS.getResult())) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @PutMapping("shop-cart")
    public Result updateCartItem(@MallToken MallUser user, @RequestBody MallShoppingCartItemUpdateParam param) {
        String result = mallShoppingCartItemService.updateShoppingCartItem(user.getUserId(), param);
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
        List<MallShoppingCartItemVO> list = mallShoppingCartItemService.getShoppingCartItemByCartItemIds(user.getUserId(), Arrays.asList(cartItemIds));
        if (cartItemIds.length < 1 || CollectionUtils.isEmpty(list) || list.size() != cartItemIds.length) {
            return ResultGenerator.genFailResult("参数异常");
        }
        int totalPrice = 0;
        for (MallShoppingCartItemVO vo : list) {
            totalPrice = totalPrice + vo.getSellingPrice() * vo.getGoodsCount();
        }
        if (totalPrice < 1) return ResultGenerator.genFailResult("价格异常");
        return ResultGenerator.genSuccessResult(list);
    }

}
