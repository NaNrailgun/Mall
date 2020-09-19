package com.nanrailgun.mall_gateway.controller;

import com.nanrailgun.config.common.Constants;
import com.nanrailgun.config.utils.Result;
import com.nanrailgun.config.utils.ResultGenerator;
import com.nanrailgun.goods_api.api.MallCarouselService;
import com.nanrailgun.goods_api.api.dto.MallIndexConfigService;
import com.nanrailgun.mall_gateway.controller.vo.MallIndexConfigVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MallIndexConfigController {

    @Reference
    private MallCarouselService mallCarouselService;

    @Reference
    private MallIndexConfigService mallIndexConfigService;

    @GetMapping("/index-infos")
    public Result getIndexConfig() {
        MallIndexConfigVO mallIndexConfigVO = new MallIndexConfigVO();
        mallIndexConfigVO.setCarousels(mallCarouselService.getIndexCarousel(Constants.INDEX_CAROUSEL_NUMBER));
        mallIndexConfigVO.setHotGoods(mallIndexConfigService.getIndexConfig(3, Constants.INDEX_GOODS_HOT_NUMBER));
        mallIndexConfigVO.setNewGoods(mallIndexConfigService.getIndexConfig(4, Constants.INDEX_GOODS_NEW_NUMBER));
        mallIndexConfigVO.setRecommendGoods(mallIndexConfigService.getIndexConfig(5, Constants.INDEX_GOODS_RECOMMOND_NUMBER));
        return ResultGenerator.genSuccessResult(mallIndexConfigVO);
    }
}
