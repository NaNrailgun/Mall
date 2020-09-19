package com.nanrailgun.mall_gateway.controller.vo;

import com.nanrailgun.goods_api.api.dto.MallIndexCarouselDTO;
import com.nanrailgun.goods_api.api.dto.MallIndexConfigGoodDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MallIndexConfigVO implements Serializable {
    private List<MallIndexCarouselDTO> carousels;

    private List<MallIndexConfigGoodDTO> hotGoods;

    private List<MallIndexConfigGoodDTO> newGoods;

    private List<MallIndexConfigGoodDTO> recommendGoods;
}
