package com.nanrailgun.goods_api.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallSearchGoodsDTO implements Serializable {
    private Long goodsId;

    private String goodsName;

    private String goodsIntro;

    private String goodsCoverImg;

    private Integer sellingPrice;
}
