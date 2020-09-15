package com.nanrailgun.mall_gateway.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MallIndexConfigVO implements Serializable {
    private List<MallIndexCarouselVO> carousels;

    private List<MallIndexConfigGoodVO> hotGoods;

    private List<MallIndexConfigGoodVO> newGoods;

    private List<MallIndexConfigGoodVO> recommendGoods;
}
