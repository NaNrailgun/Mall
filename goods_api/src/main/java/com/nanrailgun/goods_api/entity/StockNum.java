package com.nanrailgun.goods_api.entity;

import lombok.Data;

@Data
public class StockNum {
    private Long goodsId;
    private Integer goodsCount;
}
