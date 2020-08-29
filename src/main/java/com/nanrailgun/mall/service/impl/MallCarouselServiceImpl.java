package com.nanrailgun.mall.service.impl;

import com.nanrailgun.mall.controller.vo.MallIndexCarouselVO;
import com.nanrailgun.mall.dao.MallCarouselMapper;
import com.nanrailgun.mall.entity.MallCarousel;
import com.nanrailgun.mall.service.MallCarouselService;
import com.nanrailgun.mall.utils.MyBeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MallCarouselServiceImpl implements MallCarouselService {

    @Resource
    MallCarouselMapper mallCarouselMapper;

    @Override
    public List<MallIndexCarouselVO> getIndexCarousel(int number) {
        List<MallIndexCarouselVO> list = new ArrayList<>(number);
        List<MallCarousel> mallCarousels = mallCarouselMapper.getCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(mallCarousels)) {
            list = MyBeanUtil.copyList(mallCarousels, MallIndexCarouselVO.class);
        }
        return list;
    }
}
