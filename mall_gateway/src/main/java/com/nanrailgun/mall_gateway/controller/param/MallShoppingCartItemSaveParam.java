package com.nanrailgun.mall_gateway.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallShoppingCartItemSaveParam implements Serializable {

    private Integer goodsCount;

    private Long goodsId;
}
