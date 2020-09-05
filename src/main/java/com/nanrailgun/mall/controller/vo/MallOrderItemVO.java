package com.nanrailgun.mall.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallOrderItemVO implements Serializable {
    private Long goodsId;

    private Integer goodsCount;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;
}
