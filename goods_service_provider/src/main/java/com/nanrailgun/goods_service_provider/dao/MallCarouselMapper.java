package com.nanrailgun.goods_service_provider.dao;

import com.nanrailgun.goods_api.entity.MallCarousel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallCarouselMapper {

    List<MallCarousel> getCarouselsByNum(@Param("number") int number);

}
