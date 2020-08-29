package com.nanrailgun.mall.controller;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.controller.vo.MallIndexConfigVO;
import com.nanrailgun.mall.service.MallCarouselService;
import com.nanrailgun.mall.service.MallIndexConfigService;
import com.nanrailgun.mall.utils.Result;
import com.nanrailgun.mall.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MallIndexConfigController {

    @Autowired
    MallCarouselService mallCarouselService;

    @Autowired
    MallIndexConfigService mallIndexConfigService;

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
