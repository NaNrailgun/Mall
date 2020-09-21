package com.nanrailgun.goods_api.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockNum implements Serializable {
    private Long goodsId;
    private Integer goodsCount;
}
