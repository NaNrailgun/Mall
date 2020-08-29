package com.nanrailgun.mall.service;

import com.nanrailgun.mall.controller.vo.MallIndexCarouselVO;

import java.util.List;

public interface MallCarouselService {

    /**
     * 获取首页轮播图
     */
    List<MallIndexCarouselVO> getIndexCarousel(int number);
}
