package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallCarousel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallCarouselMapper {

    List<MallCarousel> getCarouselsByNum(@Param("number") int number);

}
