package com.nanrailgun.goods_service_provider.service;

import com.nanrailgun.config.utils.MyBeanUtil;
import com.nanrailgun.goods_api.api.MallCarouselService;
import com.nanrailgun.goods_api.api.dto.MallIndexCarouselDTO;
import com.nanrailgun.goods_api.entity.MallCarousel;
import com.nanrailgun.goods_service_provider.dao.MallCarouselMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MallCarouselServiceImpl implements MallCarouselService {

    @Resource
    MallCarouselMapper mallCarouselMapper;

    @Override
    public List<MallIndexCarouselDTO> getIndexCarousel(int number) {
        List<MallIndexCarouselDTO> list = new ArrayList<>(number);
        List<MallCarousel> mallCarousels = mallCarouselMapper.getCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(mallCarousels)) {
            list = MyBeanUtil.copyList(mallCarousels, MallIndexCarouselDTO.class);
        }
        return list;
    }
}
