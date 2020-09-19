package com.nanrailgun.goods_api.api;

import com.nanrailgun.goods_api.api.dto.MallIndexCarouselDTO;

import java.util.List;

public interface MallCarouselService {

    /**
     * 获取首页轮播图
     */
    List<MallIndexCarouselDTO> getIndexCarousel(int number);
}
