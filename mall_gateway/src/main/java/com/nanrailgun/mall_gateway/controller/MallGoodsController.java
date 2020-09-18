package com.nanrailgun.mall_gateway.controller;

import com.nanrailgun.config.common.Constants;
import com.nanrailgun.config.common.MallException;
import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.config.config.annotation.MallToken;
import com.nanrailgun.config.utils.PageQueryUtil;
import com.nanrailgun.config.utils.PageResult;
import com.nanrailgun.config.utils.Result;
import com.nanrailgun.config.utils.ResultGenerator;
import com.nanrailgun.goods_api.api.MallGoodsService;
import com.nanrailgun.goods_api.entity.MallGoods;
import com.nanrailgun.mall_gateway.controller.vo.MallGoodsDetailVO;
import com.nanrailgun.user_api.entity.MallUser;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MallGoodsController {

    @Reference
    private MallGoodsService mallGoodsService;

    @GetMapping("/search")
    public Result search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Long goodsCategoryId,
                         @RequestParam(required = false) String orderBy,
                         @RequestParam(required = false) Integer pageNumber,
                         @MallToken MallUser user) {
        Map param = new HashMap();
        if (goodsCategoryId == null && StringUtils.isEmpty(keyword)) {
            MallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if (pageNumber == null || pageNumber < 1) pageNumber = 1;
        param.put("goodsCategoryId", goodsCategoryId);
        param.put("page", pageNumber);
        param.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        param.put("goodsSellStatus", Constants.SELL_STATUS_UP);
        if (!StringUtils.isEmpty(keyword)) {
            param.put("keyword", keyword);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            param.put("orderBy", orderBy);
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(param);
        PageResult result = mallGoodsService.getGoodsListBySearch(pageQueryUtil, pageQueryUtil.getPage(), pageQueryUtil.getLimit());
        return ResultGenerator.genSuccessResult(result);
    }

    @GetMapping("/goods/detail/{goodsId}")
    public Result getGoodsDetail(@PathVariable("goodsId") Long goodsId, @MallToken MallUser user) {
        MallGoods goods;
        if ((goods = mallGoodsService.getGoodsDetailByGoodsId(goodsId)) == null) {
            MallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if (goods.getGoodsSellStatus() == Constants.SELL_STATUS_DOWN) {
            MallException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
        }
        MallGoodsDetailVO mallGoodsDetailVO = new MallGoodsDetailVO();
        BeanUtils.copyProperties(goods, mallGoodsDetailVO);
        mallGoodsDetailVO.setGoodsCarouselList(",".split(goods.getGoodsCarousel()));
        return ResultGenerator.genSuccessResult(mallGoodsDetailVO);
    }

}
