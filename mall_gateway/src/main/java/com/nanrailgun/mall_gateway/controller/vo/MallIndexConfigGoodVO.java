package com.nanrailgun.mall_gateway.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallIndexConfigGoodVO implements Serializable {
    private Long goodsId;
    private String goodsName;
    private String goodsIntro;
    private String goodsCoverImg;
    private Integer sellingPrice;
    private String tag;
}
