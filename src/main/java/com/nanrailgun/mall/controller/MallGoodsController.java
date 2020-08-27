package com.nanrailgun.mall.controller;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.common.MallException;
import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.config.annotation.MallToken;
import com.nanrailgun.mall.controller.vo.MallGoodsDetailVO;
import com.nanrailgun.mall.entity.MallGoods;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.service.MallGoodsService;
import com.nanrailgun.mall.utils.PageQueryUtil;
import com.nanrailgun.mall.utils.Result;
import com.nanrailgun.mall.utils.ResultGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MallGoodsController {

    @Autowired
    MallGoodsService mallGoodsService;

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
        return ResultGenerator.genSuccessResult(mallGoodsService.getGoodsListBySearch(pageQueryUtil));
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
